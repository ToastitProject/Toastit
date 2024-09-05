# Toastit

## 프로젝트 소개

**Toastit**은 다양한 칵테일을 검색하고 추천받을 수 있는 웹 애플리케이션입니다. 기본적인 칵테일 정보부터 계절별, 지역별, 날씨에 따른 추천 기능까지 제공합니다. 또한, 사용자는 자신만의 칵테일을 등록하고, 다른 사용자들과 공유할 수 있습니다.

## 기능 목록

- **Base Cocktail 관리**
  - 기본 칵테일 목록을 조회할 수 있는 기능을 제공합니다.

- **Craft Cocktail 관리**
  - 직접 만든 칵테일을 등록하고 공유할 수 있는 기능을 포함합니다.

- **Local Cocktail 정보**
  - 지역별로 추천하는 칵테일 정보를 제공하며, 위치 기반 추천 시스템을 구현합니다.

- **Seasonal Cocktail 추천**
  - 계절에 맞는 칵테일을 추천해주는 기능을 포함합니다.

- **Trend Cocktail 조회**
  - 현재 트렌드에 맞는 인기 칵테일 목록을 실시간으로 제공합니다.

- **Weather Cocktail 추천**
  - 사용자의 날씨 정보를 기반으로 맞춤형 칵테일을 추천해줍니다.

- **이미지 업로드 및 관리**
  - 칵테일 관련 이미지를 업로드하고 관리할 수 있습니다.

- **사용자 관리**
  - 사용자 인증, 권한 관리 및 사용자 정보 수정 기능을 제공합니다.

- **JWT 및 OAuth2 인증**
  - 보안을 강화하기 위해 JWT 및 OAuth2를 사용한 인증 기능을 제공합니다.

## 기술 스택

- **Backend**: Spring Boot
- **Frontend**: Thymeleaf
- **Database**: MySQL, MongoDB
- **Cache**: Redis
- **Containerization**: Docker, Docker Compose
- **Web Server**: Nginx, Nginx Proxy Manger
- **CI/CD**: GitHub Actions, Jenkins
- **Cloud Platforms**: AWS, GCP
- **DNS**: DuckDNS
- **Email**: Google SMTP
- **Notification Bots**:
  - GitHub Actions 기반 PR 알림봇
  - Jenkins 플러그인 기반 배포 알림봇
- **APIs**:
  - Naver 데이터랩 API 및 OAuth API
  - Kakao OAuth API
  - Kakao AdFit
  - Google OAuth API
  - Google Geocoding API
  - Google JavaScript Maps API
  - 기상청 API

## 프로젝트 구조

```
├── feature
│   ├── basecocktail
│   ├── craftcocktail
│   ├── image
│   ├── localcocktail
│   ├── seasonalcocktail
│   ├── trendcocktail
│   ├── user
│   └── weathercocktail
├── global
│   ├── config
│   └── entity
├── infra
│   ├── auth
│   ├── core
│   ├── email
│   ├── jwt
│   └── oauth2
├── HomeController
└── ToastitApplication
```

- `feature/`: 주요 기능들이 구현된 패키지.
  - `basecocktail/`: 기본 칵테일 관련 기능.
  - `craftcocktail/`: 사용자 제작 칵테일 관리.
  - `image/`: 이미지 업로드 및 관리.
  - `localcocktail/`: 지역별 칵테일 정보.
  - `seasonalcocktail/`: 계절별 칵테일 추천.
  - `trendcocktail/`: 트렌드 칵테일 목록 제공.
  - `user/`: 사용자 관리.
  - `weathercocktail/`: 날씨에 따른 칵테일 추천.

- `global/`: 전역 설정 및 엔티티 관리.
  - `config/`: 전역 설정 파일.
  - `entity/`: 데이터베이스 엔티티 정의.

- `infra/`: 인프라 및 보안 관련 기능.
  - `auth/`: 인증 관련 기능.
  - `core/`: 핵심 로직.
  - `email/`: 이메일 전송 기능.
  - `jwt/`: JWT 인증.
  - `oauth2/`: OAuth2 인증.

## 사용 예시 

1. **회원가입 및 로그인**
  - OAuth2를 사용하여 Naver 또는 Kakao 계정을 통해 간편하게 회원가입 및 로그인할 수 있습니다.

2. **칵테일 검색 및 추천**
  - 원하는 칵테일을 검색하거나 날씨, 계절, 지역에 따른 맞춤형 칵테일 추천을 받을 수 있습니다.

3. **자신만의 칵테일 등록 및 공유**
  - 사용자는 자신만의 칵테일 레시피를 등록하고, 다른 사용자와 공유할 수 있습니다.

4. **트렌드 칵테일 확인**
  - 지난달과 이번 달의 검색 트래픽을 기반으로 인기 있는 트렌드 칵테일 목록을 확인할 수 있습니다.

5. **무작위 칵테일 추천**
  - 사용자는 무작위로 추천받은 칵테일을 통해 새로운 레시피를 발견할 수 있습니다.

6. **좋아요 기능**
  - 마음에 드는 칵테일 레시피에 '좋아요'를 눌러 저장하고, 나중에 다시 볼 수 있습니다.

7. **팔로우 기능**
  - 다른 사용자를 팔로우하여 그들의 칵테일 레시피나 업데이트를 확인할 수 있습니다.

8. **칵테일 재료 쇼핑 링크**
  - 레시피에 필요한 칵테일 재료를 쉽게 구매할 수 있도록 쇼핑 링크를 제공합니다.

9. **위치 기반 칵테일 추천**
  - 사용자의 위치를 기반으로 지역별 칵테일을 추천받을 수 있습니다.

## 데모

배포된 데모 버전을 확인하려면 [여기](https://toastit.duckdns.org)를 클릭하세요.

## 참고자료 

- [프로젝트 세부사항](https://github.com/ToastitProject/Toastit/wiki)
- [요구사항 정의서](https://github.com/ToastitProject/Toastit/wiki/Software-Requirements-Specification(SRS))
- [데이터 모델링](https://www.erdcloud.com/d/9GAEaG2gYyCjtHvk2)
- [디자인 목업](https://www.figma.com/design/CP4NS4ogDmtTSKP2I8zyQi/%EC%84%B8%EC%83%81%EC%97%90-%EB%82%98%EC%81%9C-%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%8A%94-%EC%97%86%EB%8B%A4-team-library?node-id=0-1&t=FMWmhg01BUtuBxEv-1)
- [칸반 보드](https://github.com/your-repo/projects)
- [커밋 컨벤션](https://github.com/ToastitProject/Toastit/wiki/Commit-Convension)
- [브랜치 컨벤션](https://github.com/ToastitProject/Toastit/wiki/Branch-Convension)

## 예시화면

![홈 화면](./assets/home_screen.png)