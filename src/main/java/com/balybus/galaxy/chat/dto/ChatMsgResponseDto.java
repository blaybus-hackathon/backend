package com.balybus.galaxy.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMsgResponseDto {
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private Long patientLogId;
    private String content;
}
