package com.balybus.galaxy.member.dto.request;

import com.balybus.galaxy.login.oauth.domain.type.CaseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequest {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignInDto {
        private String userId;
        private String userPw;
    }
}
