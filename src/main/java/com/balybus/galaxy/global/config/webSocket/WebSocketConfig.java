package com.balybus.galaxy.global.config.webSocket;

import com.balybus.galaxy.global.config.jwt.webSocket.WebSocketAuthInterceptor;
import com.balybus.galaxy.global.config.jwt.webSocket.WebSocketChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final WebSocketAuthInterceptor webSocketAuthInterceptor;
    private final WebSocketChannelInterceptor channelInterceptor;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // 메시지 송신 경로 설정
        registry.enableSimpleBroker("/topic", "/queue"); // 구독 경로 설정
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat") // 웹소켓 연결 엔드포인트
                .setAllowedOriginPatterns("*")
                .addInterceptors(webSocketAuthInterceptor) // 핸드쉐이크 인터셉터 설정
                .withSockJS(); // SockJS 사용
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInterceptor); // 채널 인터셉터
    }
}
