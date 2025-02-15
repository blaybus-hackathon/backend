package com.balybus.galaxy.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    // 1xx
    INVALID_REQUEST(100, "올바르지 않은 요청입니다."),

    // 2xx 성공코드에 해당하므로 에러코드로 사용하지 않겠습니다.

    // 3xx 요양 보호사
    NOT_FOUND_HELPER(300, "요양 보호사 테이블을 찾을 수 없습니다."),
    SIGNUP_HELPER_WORK_TIME_INFO_NULL(301, "요양 보호사 근무 시간 회원 가입 정보를 다시 확인해주세요"),

    DATA_VALIDATION_ERROR(302, "데이터 무결성 위반: 필수값을 확인하세요."),
    TRANSACTIONAL_ERROR(303, "트랜잭션 처리 중 오류가 발생했습니다."),

    SIGNUP_HELPER_EXPERIENCE_INFO_NULL(3004, "요양 보호사 경력 회원 가입 정보를 다시 확인해주세요."),

    HELPER_ALREADY_HAS_WORK_TIME(305, "해당 요양 보호사는 이미 등록된 근무 시간이 있습니다."),
    HELPER_ALREADY_HAS_EXPERIENCE(306, "해당 요양 보호사는 이미 등록된 근무 경력이 있습니다."),

    // 4xx 로그인
    LOGIN_ID_EXIST(400, "로그인 아이디가 이미 존재합니다."),
    SIGNUP_INFO_NULL(401, "회원 가입 정보를 확인해주세요."),
    LOGIN_FAIL(402, "아이디/비밀번호를 확인해주세요."),

    // 5XX 서버에러
    INTERNAL_SEVER_ERROR(999, "서버에서 에러가 발생하였습니다."),

    // 6xx 파일 업로드
    UPLOAD_FAILED(600, "파일 업로드에 실패하였습니다."),
    FILE_NOT_FOUND(601, "첨부파일을 찾을 수 없습니다."),
    ;




    private final int code;
    private final String message;
}
