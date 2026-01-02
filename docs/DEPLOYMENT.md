# 배포 가이드

이 문서는 돌봄(Dolbom) 프로젝트의 배포 및 운영 방법을 안내합니다.

---

## 환경별 설정

| 환경 | 프로필 | 용도 |
|------|--------|------|
| Local | `local` | 로컬 개발 |
| Development | `dev` | 개발 서버 |
| Production | `prod` | 운영 서버 |

---

## Docker 배포

### Dockerfile

프로젝트 루트의 `Dockerfile`:

```dockerfile
FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=dev", "/app.jar"]
```

### 이미지 빌드

```bash
# 1. 애플리케이션 빌드
./gradlew clean build -x test

# 2. Docker 이미지 빌드
docker build -t dolbom:latest .

# 3. 태그 지정
docker tag dolbom:latest dolbom:v1.0.0
```

### 컨테이너 실행

```bash
# 기본 실행
docker run -d \
  --name dolbom-app \
  -p 8080:8080 \
  dolbom:latest

# 환경 변수 지정 실행
docker run -d \
  --name dolbom-app \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e MYSQL_HOST=your-db-host \
  -e MYSQL_PASSWORD=your-password \
  dolbom:latest
```

### Docker Compose (권장)

```yaml
# docker-compose.yml
version: '3.8'

services:
  app:
    build: .
    container_name: dolbom-app
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - TZ=Asia/Seoul
    depends_on:
      - mysql
      - redis
    networks:
      - dolbom-network

  mysql:
    image: mysql:8.0
    container_name: dolbom-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root1234
      MYSQL_DATABASE: galaxy
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - dolbom-network

  redis:
    image: redis:7.0
    container_name: dolbom-redis
    ports:
      - "6379:6379"
    networks:
      - dolbom-network

networks:
  dolbom-network:
    driver: bridge

volumes:
  mysql-data:
```

실행:
```bash
docker-compose up -d
```

---

## GitHub Actions CI/CD

### 워크플로우 파일

`.github/workflows/gradle.yml`:

```yaml
name: Java CI/CD with Gradle

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Build with Gradle
      run: ./gradlew build -x test

    - name: Run tests
      run: ./gradlew test

    - name: Build Docker image
      run: docker build -t dolbom:${{ github.sha }} .

    # AWS ECR 푸시 (선택)
    # - name: Push to ECR
    #   run: |
    #     aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin $ECR_REGISTRY
    #     docker push $ECR_REGISTRY/dolbom:${{ github.sha }}
```

### 시크릿 설정

GitHub Repository → Settings → Secrets and variables → Actions:

| 시크릿 이름 | 설명 |
|-------------|------|
| `AWS_ACCESS_KEY_ID` | AWS 액세스 키 |
| `AWS_SECRET_ACCESS_KEY` | AWS 시크릿 키 |
| `DB_PASSWORD` | 데이터베이스 비밀번호 |
| `JWT_SECRET` | JWT 서명 키 |

---

## AWS 인프라

### 아키텍처 구성

```
┌─────────────────────────────────────────────────────────┐
│                        AWS Cloud                         │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐  │
│  │   Route 53  │───▶│     ALB     │───▶│    EC2 /    │  │
│  │   (DNS)     │    │             │    │    ECS      │  │
│  └─────────────┘    └─────────────┘    └──────┬──────┘  │
│                                               │          │
│                     ┌─────────────────────────┼──────┐  │
│                     │                         ▼      │  │
│  ┌─────────────┐    │    ┌─────────────┐           │  │
│  │    S3       │    │    │    RDS      │           │  │
│  │  (Storage)  │    │    │  (MySQL)    │           │  │
│  └─────────────┘    │    └─────────────┘           │  │
│                     │                               │  │
│                     │    ┌─────────────┐           │  │
│                     │    │ ElastiCache │           │  │
│                     │    │  (Redis)    │           │  │
│                     │    └─────────────┘           │  │
│                     │                    VPC        │  │
│                     └───────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

### RDS (MySQL)

현재 설정:
- **Engine**: MySQL 8.0
- **Region**: ap-northeast-2 (Seoul)
- **Instance**: db.t3.micro (개발용)

연결 정보:
```yaml
# application-db.yml (dev)
spring:
  datasource:
    url: jdbc:mysql://{RDS_ENDPOINT}:3306/galaxy
    username: admin
    password: ${DB_PASSWORD}
```

### S3

현재 설정:
- **Bucket**: dolbom-s3
- **Region**: ap-northeast-2

용도:
- 프로필 이미지 저장
- 자격증 이미지 저장
- 기타 파일 저장

### Lambda (선택)

서버리스 함수 실행을 위한 Lambda 연동:
- 이미지 리사이징
- 알림 발송
- 배치 작업

---

## 모니터링

### Spring Actuator

기본 엔드포인트:

| 엔드포인트 | 설명 |
|------------|------|
| `/actuator/health` | 애플리케이션 헬스 체크 |
| `/actuator/info` | 애플리케이션 정보 |
| `/actuator/metrics` | 메트릭 정보 |
| `/actuator/prometheus` | Prometheus 메트릭 |

### Prometheus 설정

`monitoring/prometheus/config/prometheus.yml`:

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'dolbom-app'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['app:8080']
```

### 메트릭 수집

주요 메트릭:
- JVM 메모리 사용량
- HTTP 요청 수/응답 시간
- 데이터베이스 커넥션 풀
- 캐시 히트율

---

## 배포 체크리스트

### 배포 전

- [ ] 모든 테스트 통과 확인
- [ ] application-*.yml 환경 변수 확인
- [ ] 데이터베이스 마이그레이션 확인
- [ ] 시크릿 값 설정 확인
- [ ] Docker 이미지 빌드 테스트

### 배포 중

- [ ] 헬스 체크 엔드포인트 확인
- [ ] 로그 모니터링
- [ ] 트래픽 전환 확인

### 배포 후

- [ ] Swagger UI 접근 테스트
- [ ] 주요 API 동작 확인
- [ ] 모니터링 대시보드 확인
- [ ] 에러 로그 확인

---

## 롤백

### Docker 롤백

```bash
# 이전 버전으로 롤백
docker stop dolbom-app
docker rm dolbom-app
docker run -d --name dolbom-app -p 8080:8080 dolbom:v0.9.0
```

---

## 로그 관리

### 로그 위치

- **컨테이너 로그**: `docker logs dolbom-app`
- **애플리케이션 로그**: `/var/log/dolbom/`

### 로그 레벨 설정

```yaml
# application.yml
logging:
  level:
    root: INFO
    com.balybus.galaxy: DEBUG
    org.springframework.security: DEBUG
```

### 로그 조회

```bash
# 실시간 로그
docker logs -f dolbom-app

# 최근 100줄
docker logs --tail 100 dolbom-app

# 특정 시간 이후
docker logs --since 1h dolbom-app
```

---

## 참고 문서

- [개발 환경 가이드](GETTING_STARTED.md) - 로컬 실행 방법
- [아키텍처](ARCHITECTURE.md) - 시스템 구조
- [문제 해결](TROUBLESHOOTING.md) - 배포 관련 이슈
