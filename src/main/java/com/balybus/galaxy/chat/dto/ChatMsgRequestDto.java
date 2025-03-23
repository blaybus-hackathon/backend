package com.balybus.galaxy.chat.dto;

import lombok.Data;

public class ChatMsgRequestDto {
    @Data
    public static class SendPrivateMessage {
        private Long senderId;
        private Long receiverId;
        private Long patientLogId;
        private String content;
    }

    @Data
    public static class FindChatDetail {
        private int pageNo;
        private Long chatRoomId;
    }
}
