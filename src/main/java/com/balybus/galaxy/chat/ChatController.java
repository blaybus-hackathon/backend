package com.balybus.galaxy.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public TblChatMsg sendMessage(TblChatMsg message) {
        return message; // 받은 메시지를 그대로 브로드캐스트
    }

    public void sendPrivateMessage(String receiver, TblChatMsg message) {
        messagingTemplate.convertAndSendToUser(receiver, "/queue/private", message);
    }
}
