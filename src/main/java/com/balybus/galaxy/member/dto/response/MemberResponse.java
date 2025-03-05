package com.balybus.galaxy.member.dto.response;

import com.balybus.galaxy.login.domain.type.RoleType;
import lombok.Builder;
import lombok.Getter;

public class MemberResponse {
    @Builder
    @Getter
    public static class SignInDto{
        private String email;
        private RoleType userAuth;
    }
    @Builder
    @Getter
    public static class FindEmail{
        private int code;
        private String result;
    }
    @Builder
    @Getter
    public static class FindPwd{
        private int code;
        private String result;
    }
}
