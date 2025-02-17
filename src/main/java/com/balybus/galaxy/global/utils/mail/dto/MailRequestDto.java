package com.balybus.galaxy.global.utils.mail.dto;

import lombok.Builder;
import lombok.Getter;

public class MailRequestDto {
    @Getter
    @Builder
    public static class AuthenticationMail{
        private String email;
    }
    @Getter
    @Builder
    public static class CheckAuthenticationCode{
        private Long amSeq;
        private String email;
        private String code;
    }
}
