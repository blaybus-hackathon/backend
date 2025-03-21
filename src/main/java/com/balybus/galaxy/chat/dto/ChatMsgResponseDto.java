package com.balybus.galaxy.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


public class ChatMsgResponseDto {
    @Getter
    @SuperBuilder
    public static class BaseDto {
        private Long senderId;
        private Long receiverId;
        private Long patientLogId;
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
    public static class FindList extends BaseDto{
        private String senderName;
        private String receiverName;
        private String patientLogName;
    }

    @Getter
    @SuperBuilder
    public static class FindChatDetail extends BaseDto{
//        private String senderName;
//        private String receiverMail;
//        private String content;
    }
}
