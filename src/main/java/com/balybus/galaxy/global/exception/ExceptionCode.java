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

    // 9XXX
    INTERNAL_SEVER_ERROR(9999, "서버에서 에러가 발생하였습니다.");


    private final int code;
    private final String message;
}
