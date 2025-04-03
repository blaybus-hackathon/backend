package com.balybus.galaxy.global.utils.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {
    // 이메일 인증
    AUTHENTICATION("mail/sendTempAuthentication"),
    // 요양보호사 매칭 리스트
    MATCHING_LIST("mail/sendMatchingList"),
    // 임시 비밀번호 발송
    TEMP_PWD("mail/sendTempPwd"),
    ;

    private final String uri;
}
