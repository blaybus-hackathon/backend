package com.balybus.galaxy.chat;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl {
    private final TblChatMsgRepository chatMsgRepository;

    public ChatServiceImpl(TblChatMsgRepository chatMsgRepository) {
        this.chatMsgRepository = chatMsgRepository;
    }

    public TblChatMsg saveMessage(TblChatMsg message) {
        return chatMsgRepository.save(message);
    }

    public List<TblChatMsg> getChatHistory(String sender, String receiver) {
        return chatMsgRepository.findBySenderAndReceiver(sender, receiver);
    }
}
