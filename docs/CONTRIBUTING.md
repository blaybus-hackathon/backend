# 기여 가이드

돌봄(Dolbom) 프로젝트에 기여해 주셔서 감사합니다! 이 문서는 프로젝트에 기여하는 방법을 안내합니다.

---

## 목차

1. [브랜치 전략](#브랜치-전략)
2. [커밋 메시지 컨벤션](#커밋-메시지-컨벤션)
3. [PR 작성 가이드](#pr-작성-가이드)
4. [코드 리뷰 프로세스](#코드-리뷰-프로세스)
5. [이슈 템플릿](#이슈-템플릿)
6. [코드 스타일](#코드-스타일)

---

## 브랜치 전략

Git Flow를 기반으로 한 브랜치 전략을 사용합니다.

### 브랜치 구조

```
main
├── develop
│   ├── feature/#123-add-login
│   ├── feature/#124-user-profile
│   └── feature/#125-chat-function
├── release/v1.0.0
└── hotfix/#130-critical-bug
```

### 브랜치 종류

| 브랜치 | 용도 | 생성 위치 | 병합 위치 |
|--------|------|-----------|-----------|
| `main` | 운영 배포 | - | - |
| `develop` | 개발 통합 | main | main |
| `feature/*` | 기능 개발 | develop | develop |
| `release/*` | 릴리즈 준비 | develop | main, develop |
| `hotfix/*` | 긴급 수정 | main | main, develop |

### 브랜치 네이밍 규칙

```
feature/#이슈번호_간단한-설명
```

예시:
- `feature/#123-add-user-login`
- `feature/#124-helper-profile-update`
- `hotfix/#130-fix-token-expire`
- `release/v1.0.0`

### 브랜치 생성 및 작업

```bash
# 1. develop 브랜치에서 feature 브랜치 생성
git checkout develop
git pull origin develop
git checkout -b feature/#123-add-login

# 2. 작업 후 커밋
git add .
git commit -m "[Feat]: 로그인 기능 구현"

# 3. 원격에 푸시
git push origin feature/#123-add-login

# 4. PR 생성 (GitHub)
```

---

## 커밋 메시지 컨벤션

### 기본 형식

```
[타입]: 제목

본문 (선택)

푸터 (선택)
```

### 타입 종류

| 타입 | 설명 | 예시 |
|------|------|------|
| `[Feat]` | 새로운 기능 추가 | `[Feat]: 카카오 로그인 기능 추가` |
| `[Fix]` | 버그 수정 | `[Fix]: 토큰 만료 시 에러 수정` |
| `[Refactor]` | 코드 리팩토링 | `[Refactor]: 서비스 레이어 분리` |
| `[Style]` | 코드 포맷팅 | `[Style]: 코드 정렬 및 공백 수정` |
| `[Docs]` | 문서 수정 | `[Docs]: README 업데이트` |
| `[Test]` | 테스트 추가 | `[Test]: 로그인 서비스 단위 테스트` |
| `[Chore]` | 빌드, 설정 변경 | `[Chore]: Gradle 의존성 업데이트` |
| `[Design]` | UI/UX 변경 | `[Design]: 로그인 페이지 스타일 수정` |
| `[Rename]` | 파일/폴더 이름 변경 | `[Rename]: UserService → MemberService` |
| `[Remove]` | 파일/코드 삭제 | `[Remove]: 사용하지 않는 클래스 삭제` |

### 좋은 커밋 메시지 예시

```
[Feat]: 요양보호사 프로필 조회 API 구현

- HelperController에 프로필 조회 엔드포인트 추가
- HelperService에 비즈니스 로직 구현
- HelperRepository에 QueryDSL 쿼리 추가

Resolves: #123
```

### 나쁜 커밋 메시지 예시

```
fix bug          # 타입 형식 안 맞음
수정             # 무슨 수정인지 알 수 없음
WIP              # 작업 중인 커밋은 squash 필요
```

---

## PR 작성 가이드

### PR 제목

```
[타입] 간단한 설명 (#이슈번호)
```

예시:
- `[Feat] 요양보호사 프로필 조회 API 구현 (#123)`
- `[Fix] 토큰 만료 시 500 에러 수정 (#124)`

### PR 템플릿

```markdown
## 작업 내용
<!-- 이 PR에서 작업한 내용을 간단히 설명해주세요 -->

-

## 변경 사항
<!-- 주요 변경 사항을 리스트로 작성해주세요 -->

-

## 테스트
<!-- 테스트 방법을 설명해주세요 -->

- [ ] 로컬에서 테스트 완료
- [ ] 단위 테스트 통과
- [ ] API 테스트 완료

## 스크린샷 (선택)
<!-- UI 변경이 있다면 스크린샷을 첨부해주세요 -->

## 관련 이슈
<!-- 관련 이슈 번호를 작성해주세요 -->

Closes #이슈번호
```

### PR 체크리스트

- [ ] 브랜치 네이밍 규칙 준수
- [ ] 커밋 메시지 컨벤션 준수
- [ ] 코드 리뷰어 지정
- [ ] 라벨 지정 (feature, bug, docs 등)
- [ ] CI 빌드 통과

---

## 코드 리뷰 프로세스

### 리뷰 요청

1. PR 생성 후 팀원에게 리뷰 요청
2. 최소 1명 이상의 Approve 필요
3. CI 빌드 통과 필수

### 리뷰 시 확인 사항

- [ ] 코드 동작이 올바른가?
- [ ] 코드가 읽기 쉬운가?
- [ ] 중복 코드는 없는가?
- [ ] 예외 처리가 적절한가?
- [ ] 보안 취약점은 없는가?
- [ ] 성능 문제는 없는가?

### 리뷰 코멘트 작성

```
[필수] 이 부분은 반드시 수정해주세요
[권장] 이렇게 하면 더 좋을 것 같아요
[질문] 이 로직의 의도가 무엇인가요?
[칭찬] 깔끔한 코드네요!
```

---

## 이슈 템플릿

### Bug Report

```markdown
## 버그 설명
<!-- 버그에 대해 간단히 설명해주세요 -->

## 재현 방법
1.
2.
3.

## 예상 동작
<!-- 정상적으로 동작해야 하는 내용 -->

## 실제 동작
<!-- 현재 발생하는 문제 -->

## 스크린샷
<!-- 가능하다면 스크린샷 첨부 -->

## 환경
- OS:
- Browser:
- Version:
```

### Feature Request

```markdown
## 기능 설명
<!-- 추가하고 싶은 기능에 대해 설명해주세요 -->

## 필요한 이유
<!-- 이 기능이 왜 필요한지 설명해주세요 -->

## 기대 효과
<!-- 이 기능이 추가되면 어떤 효과가 있을지 -->

## 추가 정보
<!-- 참고할 만한 링크나 정보 -->
```

---

## 코드 스타일

### Java 코드 컨벤션

#### 클래스명
- PascalCase 사용
- 명사로 작성

```java
// Good
public class UserService { }
public class HelperController { }

// Bad
public class userService { }
public class Process { }
```

#### 메서드명
- camelCase 사용
- 동사로 시작

```java
// Good
public User findById(Long id) { }
public void createUser(UserDto dto) { }

// Bad
public User FindById(Long id) { }
public void user_create() { }
```

#### 변수명
- camelCase 사용
- 의미 있는 이름 사용

```java
// Good
private final UserRepository userRepository;
private String userName;

// Bad
private final UserRepository ur;
private String s;
```

### 패키지 구조

```
도메인별 패키지 구조
├── controller/    # REST API 컨트롤러
├── service/       # 비즈니스 로직
├── repository/    # 데이터 접근
├── domain/        # 엔터티
└── dto/           # 데이터 전송 객체
```

### API 응답 형식

```java
// 성공 응답
@GetMapping("/users/{id}")
public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id) {
    UserDto user = userService.findById(id);
    return ResponseEntity.ok(ApiResponse.success(user));
}

// 에러 응답
@ExceptionHandler(UserNotFoundException.class)
public ResponseEntity<ApiResponse<Void>> handleNotFound(UserNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ApiResponse.error("USER_001", e.getMessage()));
}
```

---

## 문의

궁금한 점이 있으면 이슈를 생성하거나 팀 채널로 문의해주세요!
