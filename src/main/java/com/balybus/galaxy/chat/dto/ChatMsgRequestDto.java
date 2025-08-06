package com.balybus.galaxy.chat.dto;

import lombok.Data;

public class ChatMsgRequestDto {
    @Data
    public static class SendPrivateMessage {
        private Long senderId;
        private Long receiverId;
        private Long patientLogId;
        private String content;

        private String successSepCode;
    }

    @Data
    public static class FindChatDetail {
        private int pageNo;
        private Long chatRoomId;
    }

    @Data
    public static class OutChatRoom {
        private Long chatRoomId;
    }
}
