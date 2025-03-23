package com.balybus.galaxy.chat.service;

import com.balybus.galaxy.chat.dto.ChatMsgRequestDto;
import com.balybus.galaxy.chat.dto.ChatMsgResponseDto;
import com.balybus.galaxy.chat.dto.ChatRoomResponseDto;

import java.util.List;

public interface ChatService {
    ChatMsgResponseDto.SendPrivateMessage saveMessage(ChatMsgRequestDto.SendPrivateMessage dto, String userEmail); // 전송하는 채팅 DB 저장
    List<ChatRoomResponseDto.FindList> findList(String userEmail); // 채팅방 리스트 조회
    ChatMsgResponseDto.FindChatDetail findChatDetail(ChatMsgRequestDto.FindChatDetail dto, String userEmail); // 채팅방 채팅 상세 리스트 조회
}
