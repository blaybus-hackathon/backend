package com.balybus.galaxy.chat.dto;

import lombok.Builder;
import lombok.Getter;

public class ChatRoomResponseDto {
    @Getter
    @Builder
    public static class FindList {
        private Long chatRoomId;
        private Long partnerId;
        private String partnerName;
        private Long patientLogId;
        private String patientLogName;
    }
}
