package com.balybus.galaxy.chat.dto;

import lombok.Data;

@Data
public class ChatMsgRequestDto {
    private Long senderId;
    private Long receiverId;
    private Long patientLogId;
    private String content;
}
