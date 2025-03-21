package com.balybus.galaxy.global.config.webSocket;

import com.balybus.galaxy.global.config.jwt.CookieUtils;
import com.balybus.galaxy.login.infrastructure.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {
    private final CookieUtils cookieUtils;
    private final TokenProvider tokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        //1. 쿠키에서 JWT 추출
        String accessToken = cookieUtils.getCookieForSocket(request,"JWT-TOKEN" );

        if (accessToken != null) {
            try{
                // 2. 토큰 유효성 검사
                boolean tokenValidity = tokenProvider.validateToken(accessToken);
                if(!tokenValidity) return false;

                // 3. WebSocket 세션에 사용자 정보 추가
                attributes.put("userEmail", tokenProvider.getUsername(accessToken));
                return true; // 인증 성공
            } catch(Exception e) {
                return false;
            }
        }

        return false; // 인증되지 않은 사용자는 차단
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
