# 문제 해결 가이드

이 문서는 돌봄(Dolbom) 프로젝트에서 자주 발생하는 문제와 해결 방법을 안내합니다.

---

## 목차

1. [빌드 및 실행 오류](#빌드-및-실행-오류)
2. [데이터베이스 관련](#데이터베이스-관련)
3. [인증 및 권한](#인증-및-권한)
4. [Docker 관련](#docker-관련)
5. [IDE 설정](#ide-설정)
6. [디버깅 팁](#디버깅-팁)

---

## 빌드 및 실행 오류

### Gradle 빌드 실패

#### 증상
```
> Task :compileJava FAILED
```

#### 해결 방법

1. **Java 버전 확인**
```bash
java -version
# Java 17 이상 필요
```

2. **Gradle 캐시 삭제**
```bash
./gradlew clean
rm -rf ~/.gradle/caches
./gradlew build
```

3. **의존성 새로고침**
```bash
./gradlew build --refresh-dependencies
```

---

### QueryDSL Q클래스 미생성

#### 증상
```
Cannot resolve symbol 'QTblUser'
```

#### 해결 방법

1. **Gradle 태스크 실행**
```bash
./gradlew compileQuerydsl
```

2. **IntelliJ 설정**
   - `File` → `Project Structure` → `Modules`
   - `build/generated/sources/annotationProcessor/java/main` 경로를 Sources로 추가

3. **프로젝트 새로고침**
   - Gradle 탭 → 새로고침 버튼 클릭
   - 또는 `File` → `Invalidate Caches / Restart`

---

### 포트 충돌

#### 증상
```
Web server failed to start. Port 8080 was already in use.
```

#### 해결 방법

1. **사용 중인 프로세스 확인**
```bash
# macOS / Linux
lsof -i :8080

# Windows
netstat -ano | findstr :8080
```

2. **프로세스 종료**
```bash
# macOS / Linux
kill -9 <PID>

# Windows
taskkill /PID <PID> /F
```

3. **다른 포트 사용**
```bash
./gradlew bootRun --args='--server.port=8081'
```

---

## 데이터베이스 관련

### MySQL 연결 실패

#### 증상
```
Communications link failure
The last packet sent successfully to the server was 0 milliseconds ago.
```

#### 해결 방법

1. **MySQL 서비스 확인**
```bash
# Docker
docker ps | grep mysql

# 로컬
mysql.server status
```

2. **연결 정보 확인**
```yaml
# application-db.yml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/galaxy
    username: root
    password: your_password
```

3. **방화벽/포트 확인**
```bash
telnet localhost 3306
```

---

### Redis 연결 실패

#### 증상
```
Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException
```

#### 해결 방법

1. **Redis 서비스 확인**
```bash
# Docker
docker ps | grep redis

# 로컬
redis-cli ping
# 응답: PONG
```

2. **Redis 시작**
```bash
# Docker
docker start dolbom-redis

# macOS
brew services start redis

# Linux
sudo systemctl start redis
```

3. **연결 정보 확인**
```yaml
# application-db.yml
spring:
  data:
    redis:
      host: localhost
      port: 6379
```

---

### 테이블 미존재

#### 증상
```
Table 'galaxy.tbl_user' doesn't exist
```

#### 해결 방법

1. **Hibernate DDL 설정 확인**
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # create, create-drop, update, validate, none
```

2. **데이터베이스 생성 확인**
```sql
SHOW DATABASES;
USE galaxy;
SHOW TABLES;
```

3. **수동 스키마 생성**
```bash
# 초기 스키마 적용 (있다면)
mysql -u root -p galaxy < schema.sql
```

---

## 인증 및 권한

### JWT 토큰 만료

#### 증상
```json
{
  "error": "AUTH_002",
  "message": "인증 토큰이 만료되었습니다"
}
```

#### 해결 방법

1. **토큰 갱신 요청**
```http
POST /user/token/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

2. **토큰 유효 기간 확인**
```yaml
# application-jwt.yml
jwt:
  access-expiration-time: 3600000    # 1시간
  refresh-expiration-time: 2592000000  # 30일
```

---

### 403 Forbidden

#### 증상
```
403 Forbidden - Access Denied
```

#### 해결 방법

1. **토큰 존재 확인**
```http
Authorization: Bearer {your_token}
```

2. **사용자 권한 확인**
   - 요청한 API에 필요한 권한이 있는지 확인
   - 관리자 전용 API인 경우 관리자 권한 필요

3. **토큰 유효성 확인**
   - https://jwt.io 에서 토큰 디코딩 가능

---

### OAuth2 로그인 실패

#### 증상
```
redirect_uri_mismatch
```

#### 해결 방법

1. **Kakao Developers 설정 확인**
   - 등록된 Redirect URI와 application-oauth.yml 일치 확인

2. **설정 파일 확인**
```yaml
# application-oauth.yml
kakao:
  redirect-uri: https://dolbom-work.co.kr/login/oauth2/code/kakao
```

---

## Docker 관련

### 이미지 빌드 실패

#### 증상
```
COPY failed: file not found in build context
```

#### 해결 방법

1. **JAR 파일 생성**
```bash
./gradlew clean build -x test
ls build/libs/  # JAR 파일 확인
```

2. **Dockerfile 경로 확인**
```dockerfile
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
```

---

### 컨테이너 네트워크 오류

#### 증상
```
Could not connect to Redis at redis:6379
```

#### 해결 방법

1. **Docker 네트워크 확인**
```bash
docker network ls
docker network inspect dolbom-network
```

2. **docker-compose 네트워크 설정**
```yaml
networks:
  dolbom-network:
    driver: bridge
```

3. **호스트명 대신 IP 사용**
```bash
docker inspect dolbom-redis | grep IPAddress
```

---

### 컨테이너 로그 확인

```bash
# 전체 로그
docker logs dolbom-app

# 실시간 로그
docker logs -f dolbom-app

# 최근 N줄
docker logs --tail 100 dolbom-app

# 특정 시간 이후
docker logs --since 10m dolbom-app
```

---

## IDE 설정

### Lombok 인식 오류

#### 증상
```
Cannot resolve method 'getXxx()'
```

#### 해결 방법

1. **Lombok 플러그인 설치**
   - `Settings` → `Plugins` → `Lombok` 검색 및 설치

2. **Annotation Processing 활성화**
   - `Settings` → `Build, Execution, Deployment` → `Compiler` → `Annotation Processors`
   - `Enable annotation processing` 체크

3. **IDE 재시작**

---

### Gradle 동기화 실패

#### 해결 방법

1. **캐시 삭제 및 재시작**
   - `File` → `Invalidate Caches / Restart`

2. **Gradle 설정 초기화**
```bash
rm -rf .gradle
rm -rf ~/.gradle/caches
```

3. **Gradle JVM 설정**
   - `Settings` → `Build, Execution, Deployment` → `Build Tools` → `Gradle`
   - Gradle JVM: Java 17 선택

---

## 디버깅 팁

### 로그 레벨 조정

```yaml
# application.yml
logging:
  level:
    root: INFO
    com.balybus.galaxy: DEBUG
    org.springframework.security: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

### SQL 쿼리 확인

```yaml
spring:
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

### 요청/응답 로깅

```java
// 필터 또는 인터셉터에서 로깅
@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {
        log.info("Request: {} {}", request.getMethod(), request.getRequestURI());
        filterChain.doFilter(request, response);
        log.info("Response: {}", response.getStatus());
    }
}
```

### Actuator 엔드포인트 활용

```bash
# 헬스 체크
curl http://localhost:8080/actuator/health

# 환경 정보
curl http://localhost:8080/actuator/env

# 빈 목록
curl http://localhost:8080/actuator/beans

# HTTP 요청 통계
curl http://localhost:8080/actuator/metrics/http.server.requests
```

---

## 추가 도움

문제가 해결되지 않으면:

1. [GitHub Issues](https://github.com/blaybus-hackathon/dolbom/issues)에 이슈 등록
2. 팀 Slack/Discord 채널에 문의
3. 에러 로그와 재현 방법을 함께 공유해주세요

---

## 참고 문서

- [개발 환경 가이드](GETTING_STARTED.md)
- [배포 가이드](DEPLOYMENT.md)
- [아키텍처](ARCHITECTURE.md)
