package com.balybus.galaxy.global.utils.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {
    AUTHENTICATION("mail/sendTempAuthentication"),
    MATCHING_LIST("mail/sendMatchingList"),
    ;

    private final String uri;
}
