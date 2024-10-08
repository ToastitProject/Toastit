pipeline {
    agent any

    environment {
        // Docker Hub 자격 증명
        DOCKERHUB_CREDENTIALS = credentials('docker_hub_token_for_jenkins')

        // SpringBoot 이미지 이름 정의
        IMAGE_NAME = "toastit/jdk-21-spring-boot-3.3.1"
    }

    stages {

        stage('Clean Workspace for toastit') {
            steps {
                cleanWs()
            }
        }

        stage('Checkout') {
            steps {
                git branch: "${BRANCH}",
                credentialsId: 'toastit_github_webhook_for_jenkins',
                url: 'https://github.com/ToastitProject/Toastit.git'
            }
        }

        stage('Secret Env File Download') {
            steps {
                // 애플리케이션 프로덕션 환경 설정 파일 다운로드
                withCredentials([file(credentialsId: 'toastit_env_file_for_springboot', variable: 'dbConfigFile')]) {
                    sh '''
                        pwd
                        mkdir -p ./src/main/resources/properties
                        cp $dbConfigFile ./src/main/resources/properties
                        cat $dbConfigFile
                    '''
                }

                // 애플리케이션 테스트 환경 설정 다운로드
                withCredentials([file(credentialsId: 'toastit_test-env_file_for_springboot', variable: 'dbConfigFile')]) {
                    sh '''
                        pwd
                        mkdir -p ./src/test/resources/properties
                        cp $dbConfigFile ./src/test/resources/properties
                        cat $dbConfigFile
                    '''
                }
            }
        }

        stage('Build') {
            steps {
                // 프로젝트 빌드 (테스트 제외)
                sh './gradlew clean build -x test'
            }
        }

        stage('Prepare Docker Compose File') {
            steps {
                // Secret File -> SpringBoot Docker Compose 템플릿 다운
                withCredentials([file(credentialsId: 'docker-compose-springboot', variable: 'DOCKER_COMPOSE_SPRING_TEMPLATE')]) {
                    script {
                        // SpringBoot Docker Compose 템플릿 내용을 ${BUILD_NUMBER} 로 치환 후 docker-compose-springboot.yml 파일로 저장
                        def composeTemplateContent = readFile(DOCKER_COMPOSE_SPRING_TEMPLATE)
                        def finalContent = composeTemplateContent.replace('${BUILD_NUMBER}', env.BUILD_NUMBER)
                        writeFile file: 'docker-compose-springboot.yml', text: finalContent
                    }
                }
            }
        }

        stage('Docker Build and Push') {
            steps {
                script {
                    // 액세스 토큰을 사용해 도커 허브 로그인 진행
                    sh "echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin"

                    // Docker 이미지 빌드 및 푸시
                    sh "docker build -t ${env.IMAGE_NAME}:${env.BUILD_NUMBER} ."
                    sh "docker push ${env.IMAGE_NAME}:${env.BUILD_NUMBER}"

                    // Jenkins 컨테이너에서 빌드된 Docker 이미지 삭제
                    sh "docker rmi ${env.IMAGE_NAME}:${env.BUILD_NUMBER}"

                    // Docker Resource Cash 청소
                    sh "docker system prune -a -f"
                }
            }
        }

        stage('Deploy to EC2 via SSH') {
            steps {
                // EC2 서버에 Docker Compose 파일 전송 및 배포
                sshPublisher(
                    publishers: [
                        sshPublisherDesc(
                            configName: 'api-dev-springboot', // EC2 서버 SSH 설정
                            transfers: [
                                // Docker Compose 파일 전송
                                sshTransfer(
                                    sourceFiles: 'docker-compose-springboot.yml',
                                ),
                                // Docker 컨테이너 배포 명령 실행
                                sshTransfer(
                                    execCommand: """
                                        docker-compose -f /home/ubuntu/toastit/docker/dev/docker-compose-springboot.yml down || true

                                        docker-compose -f /home/ubuntu/toastit/docker/dev/docker-compose-springboot.yml up -d

                                        docker system prune -a -f
                                    """
                                )
                            ]
                        )
                    ]
                )

            }
        }
    }

    post {
        success {
        	// credentialsId - 아까 설정한 웹훅 ID | variable - 변수명(마음대로 설정)
            withCredentials([string(credentialsId: 'toastit_webhook_for_discord', variable: 'DISCORD')]) {
                discordSend description: "SUCCESS",
                footer: "자 여러분 입장~~~~ 슈우우우웃!",
                link: env.BUILD_URL, result: currentBuild.currentResult,
                title: "Toastit CI/CD",
                webhookURL: "$DISCORD"
            }
        }

        failure {
            withCredentials([string(credentialsId: 'toastit_webhook_for_discord', variable: 'DISCORD')]) {
                        discordSend description: """
                        제목 : ${currentBuild.displayName}
                        결과 : ${currentBuild.result}
                        실행 시간 : ${currentBuild.duration / 1000}s
                        """,
                        link: env.BUILD_URL, result: currentBuild.currentResult,
                        title: "${env.JOB_NAME} : ${currentBuild.displayName} 실패",
                        webhookURL: "$DISCORD"
            }
        }
    }
}