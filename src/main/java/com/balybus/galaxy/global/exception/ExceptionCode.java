package com.balybus.galaxy.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    // 1XXX
    INVALID_REQUEST(1000, "올바르지 않은 요청입니다."),

    // 2XXX
    LOGIN_ID_EXIST(2000, "로그인 아이디가 이미 존재합니다."),
    SIGNUP_INFO_NULL(2001, "회원 가입 정보를 확인해주세요."),
    LOGIN_FAIL(2002, "아이디/비밀번호를 확인해주세요."),

    // 3xxx 요양 보호사
    NOT_FOUND_HELPER(3000, "요양 보호사 테이블을 찾을 수 없습니다."),
    SIGNUP_HELPER_WORK_TIME_INFO_NULL(3001, "요양 보호사 근무 시간 회원 가입 정보를 다시 확인해주세요"),

    DATA_VALIDATION_ERROR(3002, "데이터 무결성 위반: 필수값을 확인하세요."),
    TRANSACTIONAL_ERROR(3003, "트랜잭션 처리 중 오류가 발생했습니다."),

    SIGNUP_HELPER_EXPERIENCE_INFO_NULL(3004, "요양 보호사 경력 회원 가입 정보를 다시 확인해주세요."),

    // 9XXX
    INTERNAL_SEVER_ERROR(9999, "서버에서 에러가 발생하였습니다.");


    private final int code;
    private final String message;
}
