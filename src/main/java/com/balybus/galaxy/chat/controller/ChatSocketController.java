package com.balybus.galaxy.chat.controller;

import com.balybus.galaxy.global.domain.tblChatMsg.TblChatMsg;
import com.balybus.galaxy.chat.dto.ChatMsgRequestDto;
import com.balybus.galaxy.chat.dto.ChatMsgResponseDto;
import com.balybus.galaxy.chat.service.ChatServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatServiceImpl chatService;

    // /topic/messages 를 구독중인 전체 사용자에게 브로드캐스팅
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public TblChatMsg sendMessage(TblChatMsg message) {
        return message; // 받은 메시지를 그대로 브로드캐스트
    }

    // 특정 사용자에게 1:1 메시지 전송
    @MessageMapping("/private-message")
    public void sendPrivateMessage(ChatMsgRequestDto.SendPrivateMessage message, @Headers Map<String, Object> headers) {
        Principal simpUser = (Principal) headers.get("simpUser");
        String sender = simpUser.getName();
        try {
            ChatMsgResponseDto.SendPrivateMessage responseDto = chatService.saveMessage(message, sender);   //채팅 데이터 DB 저장
            messagingTemplate.convertAndSendToUser(responseDto.getReceiverMail(), "/queue/private", responseDto); //받는사람에게 채팅 전송
            messagingTemplate.convertAndSendToUser(sender, "/queue/success",
                    ChatMsgResponseDto.resultNotice.builder()
                            .code(200)
                            .msg("SUCCESS")
                            .successSepCode(message.getSuccessSepCode())
                            .build()); //보낸사람에게 채팅 전송 성공여부 전송
        } catch (Exception e) {
            // 예외가 발생한 경우 메시지를 보낸 사람에게 에러 메시지를 보냄
            messagingTemplate.convertAndSendToUser(sender, "/queue/error",
                    ChatMsgResponseDto.resultNotice.builder()
                        .code(400)
                        .msg(e.getMessage())
                        .successSepCode(message.getSuccessSepCode())
                        .build());
        }
    }
}
