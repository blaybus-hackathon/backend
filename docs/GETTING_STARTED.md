# 개발 환경 가이드

이 문서는 돌봄(Dolbom) 프로젝트의 로컬 개발 환경 구축 방법을 안내합니다.

---

## 필수 요구사항

| 도구 | 버전 | 비고 |
|------|------|------|
| Java | 17+ | OpenJDK 권장 |
| MySQL | 8.0+ | 로컬 또는 Docker |
| Redis | 6.0+ | 로컬 또는 Docker |
| Gradle | 8.0+ | Wrapper 포함 |
| Git | 2.0+ | - |

### 권장 IDE
- **IntelliJ IDEA** (Ultimate 또는 Community)

---

## 1. 저장소 클론

```bash
git clone https://github.com/blaybus-hackathon/dolbom.git
cd dolbom
```

---

## 2. 데이터베이스 설정

### Option A: Docker 사용 (권장)

```bash
# MySQL 컨테이너 실행
docker run -d \
  --name dolbom-mysql \
  -e MYSQL_ROOT_PASSWORD=root1234 \
  -e MYSQL_DATABASE=galaxy \
  -p 3306:3306 \
  mysql:8.0

# Redis 컨테이너 실행
docker run -d \
  --name dolbom-redis \
  -p 6379:6379 \
  redis:7.0
```

### Option B: 로컬 설치

#### MySQL
```sql
-- 데이터베이스 생성
CREATE DATABASE galaxy CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 사용자 생성 (선택)
CREATE USER 'dolbom'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON galaxy.* TO 'dolbom'@'localhost';
FLUSH PRIVILEGES;
```

#### Redis
```bash
# macOS
brew install redis
brew services start redis

# Ubuntu
sudo apt-get install redis-server
sudo systemctl start redis
```

---

## 3. 환경 설정 파일

### 설정 파일 구조

```
src/main/resources/
├── application.yml           # 메인 설정
├── application-db.yml        # DB 설정 (MySQL, Redis)
├── application-jwt.yml       # JWT 설정
├── application-oauth.yml     # OAuth2 설정 (Kakao)
├── application-mail.yml      # 이메일 설정
└── application-aws.yml       # AWS 설정
```

### 프로필 설정

`application.yml`에서 활성 프로필을 설정합니다:

```yaml
spring:
  profiles:
    active: local  # local, dev, prod 중 선택
    include: db, jwt, oauth, mail, aws
```

### 로컬 DB 설정 예시

`application-db.yml`에서 로컬 환경 설정:

```yaml
spring:
  config:
    activate:
      on-profile: local
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/galaxy?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: root
    password: root1234
  data:
    redis:
      host: localhost
      port: 6379
```

---

## 4. 빌드 및 실행

### Gradle 빌드

```bash
# 전체 빌드
./gradlew build

# 테스트 스킵 빌드
./gradlew build -x test

# clean 빌드
./gradlew clean build
```

### 애플리케이션 실행

```bash
# Gradle로 실행
./gradlew bootRun

# 또는 JAR 직접 실행
java -jar build/libs/galaxy-0.0.1-SNAPSHOT.jar
```

### 프로필 지정 실행

```bash
# local 프로필로 실행
./gradlew bootRun --args='--spring.profiles.active=local'

# 또는
java -jar build/libs/galaxy-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

---

## 5. 실행 확인

### 헬스 체크

```bash
curl http://localhost:8080/actuator/health
```

응답 예시:
```json
{
  "status": "UP"
}
```

### Swagger UI 접근

브라우저에서 아래 URL로 접속:
```
http://localhost:8080/swagger-ui.html
```

---

## 6. IDE 설정 (IntelliJ IDEA)

### Lombok 플러그인

1. `Settings` → `Plugins` → `Lombok` 검색 및 설치
2. `Settings` → `Build, Execution, Deployment` → `Compiler` → `Annotation Processors`
3. `Enable annotation processing` 체크

### QueryDSL 설정

빌드 후 생성된 Q클래스 인식을 위해:

1. `File` → `Project Structure` → `Modules`
2. `build/generated/sources/annotationProcessor/java/main` 경로를 Sources로 추가

또는 Gradle 탭에서:
```
Tasks → other → compileQuerydsl
```

### 실행 구성

1. `Run` → `Edit Configurations`
2. `+ Add New Configuration` → `Spring Boot`
3. 설정:
   - **Main class**: `com.balybus.galaxy.GalaxyApplication`
   - **Active profiles**: `local`
   - **JRE**: Java 17

---

## 7. 개발 도구

### API 테스트

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **Postman**: API 테스트 도구
- **curl**: CLI 기반 테스트

### 데이터베이스 클라이언트

- **DBeaver**: 무료 DB 클라이언트
- **MySQL Workbench**: MySQL 공식 도구
- **DataGrip**: JetBrains DB IDE

### Redis 클라이언트

```bash
# Redis CLI
redis-cli

# 키 조회
KEYS *

# 특정 키 값 조회
GET key_name
```

---

## 8. 주요 엔드포인트

| 경로 | 설명 |
|------|------|
| `/swagger-ui.html` | API 문서 (Swagger UI) |
| `/actuator/health` | 헬스 체크 |
| `/actuator/info` | 애플리케이션 정보 |
| `/actuator/prometheus` | Prometheus 메트릭 |

---

## 다음 단계

- [API 문서](API.md) - API 사용법
- [아키텍처](ARCHITECTURE.md) - 시스템 구조 이해
- [기여 가이드](../CONTRIBUTING.md) - 개발 참여 방법
