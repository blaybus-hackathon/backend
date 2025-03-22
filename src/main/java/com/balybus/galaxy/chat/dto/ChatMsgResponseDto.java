package com.balybus.galaxy.chat.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;


public class ChatMsgResponseDto {
    @Getter
    @SuperBuilder
    public static class BaseDto {
        private Long patientLogId;
        private Long senderId;
        private Long receiverId;
    }

    @Getter
    @SuperBuilder
    public static class SendPrivateMessage extends BaseDto{
        private String senderName;
        private String receiverMail;
        private String content;
    }

    @Getter
    @SuperBuilder
    public static class FindChatDetail extends BaseDto{
//        private String senderName;
//        private String receiverMail;
//        private String content;
    }
}
