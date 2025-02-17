package com.balybus.galaxy.global.utils.mail.dto;

import lombok.Builder;
import lombok.Getter;

public class MailResponseDto {
    @Getter
    @Builder
    public static class AuthenticationMail{
        private Long mailSeq;
    }
}
