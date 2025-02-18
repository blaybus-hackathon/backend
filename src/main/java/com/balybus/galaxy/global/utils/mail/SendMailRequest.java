package com.balybus.galaxy.global.utils.mail;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
public class SendMailRequest {
    private final String fromName; //보내는 사람
    private final String toMail; //받는 메일
    private final String title; //제목
    private final ContentType contentType; //이메일 html uri
    private final String content; //내용

    public static class SendMailRequestBuilder {
        public SendMailRequestBuilder title(String title) {
            this.title = "[돌봄워크] " + title; // 빌더의 필드(title) 직접 수정
            return this;
        }
    }
}
