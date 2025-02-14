package com.balybus.galaxy.member.dto.response;

import com.balybus.galaxy.login.domain.type.RoleType;
import lombok.Builder;
import lombok.Getter;

public class MemberResponse {
    @Builder
    @Getter
    public static class SignInDto{
        private String accessToken;
        private String refreshToken;
        private RoleType userAuth;
    }
}
