package com.balybus.galaxy.chat.service;

import com.balybus.galaxy.chat.dto.ChatMsgRequestDto;
import com.balybus.galaxy.chat.dto.ChatMsgResponseDto;

import java.util.List;

public interface ChatService {
    ChatMsgResponseDto.SendPrivateMessage saveMessage(ChatMsgRequestDto.SendPrivateMessage dto, String userEmail); // 전송하는 채팅 DB 저장
    List<ChatMsgResponseDto.FindList> findList(String userEmail); // 채팅방 리스트 조회
    List<ChatMsgResponseDto.FindChatDetail> findChatDetail(ChatMsgRequestDto.FindChatDetail dto); // 채팅방 채팅 상세 리스트 조회
}
