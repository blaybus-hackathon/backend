package com.balybus.galaxy.member.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginType {
    KAKAO_LOGIN("KAKAO_LOGIN", "카카오 로그인"),
    DOLBOM_LOGIN("REGULAR_LOGIN", "일반 로그인");

    private final String code;
    private final String displayName;
}
