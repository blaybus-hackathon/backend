package com.balybus.galaxy.global.utils.mail;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SendMailRequest {
    private String fromName; //보내는 사람
    private String toMail; //받는 메일
    private String title; //제목
}
