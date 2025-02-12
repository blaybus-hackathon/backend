package com.balybus.galaxy.login.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoleType {
    MEMBER("MEMBER", "일반 사용자 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한"),
    NONE("NONE", "무권한");

    private final String code;
    private final String displayName;

    public static RoleType of(String code) {
        return Arrays.stream(RoleType.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()
                .orElse(NONE);
    }
}
