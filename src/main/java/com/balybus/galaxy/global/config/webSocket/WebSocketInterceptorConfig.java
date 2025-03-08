package com.balybus.galaxy.global.config.webSocket;

import com.balybus.galaxy.global.config.jwt.webSocket.WebSocketAuthInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Component
public class WebSocketInterceptorConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(), "/ws-chat")
                .addInterceptors(new WebSocketAuthInterceptor())
                .setAllowedOrigins("*");
    }
}
