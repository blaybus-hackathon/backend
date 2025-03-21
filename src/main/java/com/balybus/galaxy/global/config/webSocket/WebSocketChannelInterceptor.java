package com.balybus.galaxy.global.config.webSocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import org.springframework.stereotype.Component;

@Component
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 메시지 전송 전 처리할 로직 (예: 인증 체크)
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        // 메시지가 전송된 후 처리할 로직
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        // 메시지 전송 완료 후 처리할 로직
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        // 메시지 수신 전 처리할 로직
        return true;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        // 메시지 수신 후 처리할 로직
        return message;
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        // 메시지 수신 완료 후 처리할 로직
    }
}
