package com.balybus.galaxy.login.classic.dto.response;

import com.balybus.galaxy.login.classic.domain.type.RoleType;
import lombok.Builder;
import lombok.Getter;

public class LoginResponseDto {
    @Builder
    @Getter
    public static class SignInDto{
        private RoleType userAuth;
        private Long seq;
    }
}
