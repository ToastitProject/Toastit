# Jenkins 베이스 이미지 설정한다.
FROM jenkins/jenkins:2.452.2-lts

# 루트 사용자로 전환하여 필요한 패키지 설치를 진행한다.
USER root

# 필요한 일반 패키지들을 설치한다.
RUN apt-get update && \
    apt-get install -y \
    curl \
    wget \
    python3 \
    python3-venv \
    zip \
    gnupg \
    software-properties-common

# Oracle JDK 21 다운로드 URL
ENV JDK_URL=https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.tar.gz

# OpenJDK 21을 설치한다.
RUN wget $JDK_URL -O jdk-21_linux-x64_bin.tar.gz && \
    tar -xvf jdk-21_linux-x64_bin.tar.gz && \
    JDK_DIR=$(tar -tf jdk-21_linux-x64_bin.tar.gz | head -n 1 | cut -f1 -d"/") && \
    mv $JDK_DIR /usr/local/jdk-21 && \
    ln -sf /usr/local/jdk-21/bin/java /usr/bin/java && \
    ln -sf /usr/local/jdk-21/bin/javac /usr/bin/javac && \
    rm jdk-21_linux-x64_bin.tar.gz

# Docker 설치를 위한 준비 과정 및 Docker CLI 설치한다.
RUN apt-get update && \
    apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg2 \
    software-properties-common

# Docker GPG 키를 추가하고 Docker 리포지토리를 설정한다.
RUN curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list && \
    apt-get update && \
    apt-get install -y docker-ce-cli

# Docker Compose 설치한다.
RUN curl -L "https://github.com/docker/compose/releases/download/v2.11.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose

# 먼저 998 GID를 가진 그룹이 있는지 확인하고, 없으면 생성한다.
RUN if ! getent group 998; then groupadd -g 998 docker; else groupmod -n docker $(getent group 998 | cut -d: -f1); fi

# 이제 jenkins 사용자를 docker 그룹에 추가한다.
RUN usermod -aG docker jenkins

# Python 가상 환경 생성 및 AWS CLI 설치한다.
RUN python3 -m venv /opt/venv && \
    /opt/venv/bin/pip install --upgrade pip && \
    /opt/venv/bin/pip install awscli

# JAVA_HOME 환경 변수를 설정한다.
ENV JAVA_HOME=/usr/local/jdk-21
ENV PATH=$JAVA_HOME/bin:/opt/venv/bin:$PATH

# Jenkins 사용자로 다시 전환한다.
USER jenkins