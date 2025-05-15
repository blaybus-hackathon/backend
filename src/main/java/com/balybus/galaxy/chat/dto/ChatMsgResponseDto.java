package com.balybus.galaxy.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class ChatMsgResponseDto {
    @Getter
    @Builder
    public static class SendPrivateMessage {
        private Long patientLogId;
        private Long senderId;
        private Long receiverId;

        private String senderName;
        private String receiverMail;
        private String content;
    }

    @Getter
    @Builder
    public static class FindChatDetail {
        private boolean hasNext;
        private String partnerImgAddress;
        private List<FindChatDetailList> list;
    }

    @Getter
    @Builder
    public static class FindChatDetailList {
        private boolean senderYn;
        private String content;
        private LocalDateTime sendTime;
        private boolean readYn;
    }
}
