package com.balybus.galaxy.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    LOGIN_SUCCESS(200, "로그인 되었습니다."),
    LOGOUT_SUCCESS(200, "로그아웃 되었습니다.");

    private final Integer code;
    private final String message;
}
