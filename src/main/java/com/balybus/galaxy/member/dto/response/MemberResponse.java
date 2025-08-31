package com.balybus.galaxy.member.dto.response;

import com.balybus.galaxy.login.classic.domain.type.RoleType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

public class MemberResponse {
    @Builder
    @Getter
    public static class SignInDto{
        private Long chatSenderId;
        private String email;
        private RoleType userAuth;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Long helperSeq;  //요양보호사 구분자
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Long cmSeq;      //센터관리자 구분자
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
