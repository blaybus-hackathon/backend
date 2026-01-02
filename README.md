<p align="center">
  <h1 align="center">돌봄워크 (Dolbomwork)</h1>
  <p align="center">
    <strong>요양보호사와 돌봄이 필요한 분을 연결하는 매칭 플랫폼</strong>
  </p>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk&logoColor=white" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4.2-6DB33F?style=flat-square&logo=springboot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql&logoColor=white" alt="MySQL"/>
  <img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white" alt="Redis"/>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white" alt="Docker"/>
  <img src="https://img.shields.io/badge/AWS-232F3E?style=flat-square&logo=amazonaws&logoColor=white" alt="AWS"/>
</p>

---

## 프로젝트 소개

**돌봄(Dolbom)** 은 요양보호사와 돌봄이 필요한 환자(또는 보호자)를 효율적으로 매칭해주는 플랫폼입니다.

### 핵심 기능

| 기능 | 설명 |
|------|------|
| **요양보호사 매칭** | 환자의 요구사항과 요양보호사의 조건을 분석하여 최적의 매칭 제공 |
| **실시간 채팅** | WebSocket 기반의 실시간 채팅으로 원활한 소통 지원 |
| **프로필 관리** | 요양보호사의 경력, 자격증, 근무 조건 등 상세 프로필 관리 |
| **센터 관리** | 요양 센터 정보 관리 및 센터 관리자 기능 |
| **OAuth2 로그인** | 카카오 소셜 로그인 지원 |

---

## 기술 스택

### Backend
- **Language**: Java 17
- **Framework**: Spring Boot 3.4.2, Spring Security, Spring Data JPA
- **Authentication**: JWT, OAuth2 (Kakao)
- **Database**: MySQL 8.0, Redis
- **ORM**: JPA + QueryDSL

### Infrastructure
- **Cloud**: AWS (RDS, S3, Lambda)
- **Container**: Docker
- **CI/CD**: GitHub Actions
- **Monitoring**: Prometheus, Spring Actuator

### Communication
- **Real-time**: WebSocket + STOMP
- **API Docs**: Swagger (SpringDoc OpenAPI)

---

## 빠른 시작

### 필수 요구사항

- Java 17+
- MySQL 8.0+
- Redis 6.0+
- Gradle 8.0+

### 로컬 실행

```bash
# 1. 저장소 클론
git clone https://github.com/blaybus-hackathon/dolbom.git
cd dolbom

# 2. 환경 설정 (application-*.yml 파일 설정 필요)
# 상세 내용은 docs/GETTING_STARTED.md 참고

# 3. 빌드 및 실행
./gradlew build
./gradlew bootRun
```

### Swagger UI 접근
```
http://localhost:8080/swagger-ui.html
```

> 상세한 개발 환경 설정은 [개발 환경 가이드](docs/GETTING_STARTED.md)를 참고하세요.

---

## 문서

> 전체 문서는 [문서 센터](docs/INDEX.md)에서 확인할 수 있습니다.

| 문서 | 설명 |
|------|------|
| [문서 센터](docs/INDEX.md) | 전체 문서 네비게이션 |
| [아키텍처](docs/ARCHITECTURE.md) | 시스템 구조, 디렉토리 구조, 도메인 설명 |
| [개발 환경 가이드](docs/GETTING_STARTED.md) | 로컬 개발 환경 구축 방법 |
| [API 문서](docs/API.md) | API 엔드포인트 및 사용법 |
| [배포 가이드](docs/DEPLOYMENT.md) | Docker, CI/CD, AWS 배포 방법 |
| [기여 가이드](docs/CONTRIBUTING.md) | 브랜치 전략, 커밋 컨벤션 |
| [문제 해결](docs/TROUBLESHOOTING.md) | 자주 발생하는 문제와 해결 방법 |

---

## 프로젝트 자료

### 기획 및 협업

| 자료 | 링크 |
|------|------|
| MVP 기획 문서 | [Notion](https://future-level-ab5.notion.site/MVP-62e98829ddb54005a77c8e40ef270aa6) |
| 프로젝트 노션 | [Notion](https://blaybus.notion.site/?pvs=73) |

### 설계 문서

| 자료 | 링크 |
|------|------|
| ERD | [ERDCloud](https://www.erdcloud.com/d/Ps5YSRwrTa7Jfwgk4) |
| WBS | [Notion](https://www.notion.so/2025-195cfd33022780dcab69c54d21d85e1a?pvs=4) |

---

## 팀 소개

### 은하수 개발단

돌봄 서비스를 통해 더 나은 요양 생태계를 만들어가는 팀입니다.

---

## 라이선스

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
