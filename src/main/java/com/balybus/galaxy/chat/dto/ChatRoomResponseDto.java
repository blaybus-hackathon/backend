package com.balybus.galaxy.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class ChatRoomResponseDto {
    @Getter
    @Builder
    public static class FindList {
        private Long chatRoomId;                    // 채팅방 구분자

        private Long partnerId;                     // 채팅 상대 구분자
        private String partnerName;                 // 채팅 상대 이름
        private String partnerImgAddress;           // 채팅 상대 프로필 이미지 주소

        private String lastChatContent;             // 채팅방 마지막 대화
        private LocalDateTime lastChatSendTime;     // 채팅방 마지막 대화 전송 시간
        private Long notReadCnt;                    // 읽지 않은 대화 개수

        private Long patientLogId;                  // 어르신 공고 구분자
        private String patientLogName;              // 어르신 공고에 기록된 어르신 성함
    }
}