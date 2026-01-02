# API 문서

이 문서는 돌봄(Dolbom) 플랫폼의 API 엔드포인트를 설명합니다.

---

## Swagger UI

### 접근 방법

개발 서버 실행 후 아래 URL에서 전체 API 문서를 확인할 수 있습니다:

```
http://localhost:8080/swagger-ui.html
```

### Swagger 설정

```yaml
# application.yml
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
  api-docs:
    path: /api-docs
```

---

## 인증 방식

### JWT (JSON Web Token)

모든 인증이 필요한 API는 JWT 토큰을 사용합니다.

#### 토큰 구조

| 토큰 타입 | 유효 기간 | 용도 |
|----------|----------|------|
| Access Token | 1시간 | API 인증 |
| Refresh Token | 30일 | Access Token 갱신 |

#### 요청 헤더

```http
Authorization: Bearer {access_token}
```

### OAuth2 (Kakao)

카카오 소셜 로그인을 지원합니다.

#### 로그인 흐름
1. 클라이언트 → `/oauth2/authorization/kakao` 요청
2. 카카오 로그인 페이지로 리다이렉트
3. 로그인 성공 → 콜백 URL로 JWT 토큰 반환

---

## API 카테고리

### 1. 인증 (Authentication)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| POST | `/login` | 일반 로그인 | X |
| POST | `/signup` | 회원가입 | X |
| GET | `/oauth2/authorization/kakao` | 카카오 로그인 | X |
| POST | `/user/token/refresh` | 토큰 갱신 | O |
| POST | `/user/logout` | 로그아웃 | O |

#### 로그인 요청 예시

```http
POST /login HTTP/1.1
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

#### 로그인 응답 예시

```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600
  }
}
```

---

### 2. 요양보호사 (Helper)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/helper/profile` | 내 프로필 조회 | O |
| PUT | `/helper/profile` | 프로필 수정 | O |
| GET | `/helper/{id}` | 요양보호사 상세 조회 | O |
| GET | `/helper/list` | 요양보호사 목록 조회 | O |
| POST | `/helper/certificate` | 자격증 등록 | O |
| PUT | `/helper/work-time` | 근무 시간 설정 | O |
| PUT | `/helper/work-location` | 근무 지역 설정 | O |

#### 프로필 조회 응답 예시

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "홍길동",
    "email": "hong@example.com",
    "phone": "010-1234-5678",
    "gender": "M",
    "birthDate": "1980-01-01",
    "address": {
      "addressFirst": "서울특별시",
      "addressSecond": "강남구",
      "addressThird": "역삼동"
    },
    "certificates": [
      {
        "id": 1,
        "name": "요양보호사 자격증",
        "issueDate": "2020-01-01"
      }
    ],
    "experience": {
      "hasExperience": true,
      "years": 5,
      "details": "요양원 근무 경력"
    },
    "workTime": {
      "preferredDays": ["MON", "TUE", "WED", "THU", "FRI"],
      "preferredHours": "09:00-18:00"
    },
    "salary": {
      "type": "HOURLY",
      "amount": 15000,
      "negotiable": true
    }
  }
}
```

---

### 3. 환자 (Patient)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/patient/basic` | 환자 정보 조회 | O |
| POST | `/patient/basic` | 환자 정보 등록 | O |
| PUT | `/patient/basic/{id}` | 환자 정보 수정 | O |
| DELETE | `/patient/basic/{id}` | 환자 정보 삭제 | O |

---

### 4. 모집 (Recruit)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/patient/recruit` | 모집 공고 목록 | O |
| POST | `/patient/recruit` | 모집 공고 등록 | O |
| GET | `/patient/recruit/{id}` | 모집 공고 상세 | O |
| PUT | `/patient/recruit/{id}` | 모집 공고 수정 | O |
| DELETE | `/patient/recruit/{id}` | 모집 공고 삭제 | O |

---

### 5. 매칭 (Matching)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/patient/matching` | 매칭 상태 조회 | O |
| POST | `/patient/matching` | 매칭 요청 | O |
| PUT | `/patient/matching/{id}/accept` | 매칭 수락 | O |
| PUT | `/patient/matching/{id}/reject` | 매칭 거절 | O |
| PUT | `/patient/matching/{id}/complete` | 매칭 완료 | O |

---

### 6. 채팅 (Chat)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/chat/rooms` | 채팅방 목록 | O |
| POST | `/chat/rooms` | 채팅방 생성 | O |
| GET | `/chat/rooms/{roomId}/messages` | 메시지 조회 | O |

#### WebSocket 연결

```javascript
// STOMP over WebSocket
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
  // 채팅방 구독
  stompClient.subscribe('/topic/chat/{roomId}', function(message) {
    console.log(JSON.parse(message.body));
  });
});

// 메시지 전송
stompClient.send('/app/chat.sendMessage', {}, JSON.stringify({
  roomId: 1,
  content: '안녕하세요!'
}));
```

---

### 7. 센터 관리자 (Center Manager)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/centerManager/info` | 센터 정보 조회 | O |
| PUT | `/centerManager/info` | 센터 정보 수정 | O |
| GET | `/centerManager/patients` | 관리 환자 목록 | O |
| GET | `/centerManager/helpers` | 소속 요양보호사 목록 | O |

---

### 8. 주소 (Address)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/address/first` | 시도 목록 | X |
| GET | `/address/second/{firstId}` | 시군구 목록 | X |
| GET | `/address/third/{secondId}` | 읍면동 목록 | X |

---

### 9. 공통 (Common)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/common/care-types` | 케어 종류 목록 | X |
| POST | `/common/upload/image` | 이미지 업로드 | O |
| GET | `/common/codes/{type}` | 코드 데이터 조회 | X |

---

## 응답 형식

### 성공 응답

```json
{
  "success": true,
  "data": { ... },
  "message": "성공"
}
```

### 에러 응답

```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "에러 메시지"
  }
}
```

---

## 에러 코드

### 인증 관련 (AUTH)

| 코드 | HTTP 상태 | 설명 |
|------|----------|------|
| `AUTH_001` | 401 | 인증 토큰이 없습니다 |
| `AUTH_002` | 401 | 인증 토큰이 만료되었습니다 |
| `AUTH_003` | 401 | 유효하지 않은 토큰입니다 |
| `AUTH_004` | 403 | 접근 권한이 없습니다 |
| `AUTH_005` | 400 | 이메일 또는 비밀번호가 일치하지 않습니다 |

### 사용자 관련 (USER)

| 코드 | HTTP 상태 | 설명 |
|------|----------|------|
| `USER_001` | 404 | 사용자를 찾을 수 없습니다 |
| `USER_002` | 409 | 이미 존재하는 이메일입니다 |
| `USER_003` | 400 | 유효하지 않은 사용자 정보입니다 |

### 매칭 관련 (MATCHING)

| 코드 | HTTP 상태 | 설명 |
|------|----------|------|
| `MATCHING_001` | 404 | 매칭 정보를 찾을 수 없습니다 |
| `MATCHING_002` | 400 | 이미 진행 중인 매칭이 있습니다 |
| `MATCHING_003` | 400 | 매칭 상태를 변경할 수 없습니다 |

### 서버 에러 (SERVER)

| 코드 | HTTP 상태 | 설명 |
|------|----------|------|
| `SERVER_001` | 500 | 내부 서버 오류 |
| `SERVER_002` | 503 | 서비스 일시 중단 |

---

## 페이지네이션

목록 조회 API는 페이지네이션을 지원합니다.

### 요청 파라미터

| 파라미터 | 타입 | 기본값 | 설명 |
|----------|------|--------|------|
| `page` | int | 0 | 페이지 번호 (0부터 시작) |
| `size` | int | 20 | 페이지 크기 |
| `sort` | string | - | 정렬 기준 (예: `createdAt,desc`) |

### 응답 형식

```json
{
  "success": true,
  "data": {
    "content": [ ... ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "offset": 0
    },
    "totalPages": 5,
    "totalElements": 100,
    "first": true,
    "last": false
  }
}
```

---

## 참고 문서

- [Swagger UI](http://localhost:8080/swagger-ui.html) - 상세 API 명세
- [아키텍처](ARCHITECTURE.md) - 시스템 구조
- [개발 환경 가이드](GETTING_STARTED.md) - 로컬 실행 방법