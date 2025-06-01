package com.balybus.galaxy.global.domain.tblAuthenticationMail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TblAuthenticationMailMsgEnum {
    REGISTED_EMAIL(200, "가입된 이메일입니다."),
    KAKAO_EMAIL(201, "카카오 로그인이 진행된 이메일입니다."),

    UNREGISTED_EMAIL(404, "미가입한 이메일입니다."),
    ;
    private final int code;
    private final String msg;
}
