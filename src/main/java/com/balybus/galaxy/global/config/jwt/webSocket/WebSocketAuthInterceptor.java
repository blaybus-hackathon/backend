package com.balybus.galaxy.global.config.jwt.webSocket;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof HttpServletRequest httpServletRequest) {
            HttpSession session = httpServletRequest.getSession(false);
            if (session != null && session.getAttribute("USER") != null) {
                attributes.put("USER", session.getAttribute("USER"));
                return true;
            }
        }
        return false; // 인증되지 않은 사용자는 차단
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
