package com.balybus.galaxy.global.config.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CookieUtils {
    @Value("${jwt.front-domain}")
    private String frontDomain;

    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public void saveCookie(HttpServletResponse response, String newAccessToken) {
        // JWT 토큰을 HttpOnly 쿠키로 설정
//        ResponseCookie jwtCookie = ResponseCookie.from("JWT-TOKEN", newAccessToken)
//                .httpOnly(true)     // 클라이언트에서 쿠키를 읽을 수 없도록 설정
//                .secure(true)       // HTTPS에서만 전송하도록 설정 (개발 환경에서는 false)
//                .sameSite("Strict") // SameSite 설정을 통해 CSRF 공격 방어
//                .path("/")          // 쿠키 경로 설정
//                .maxAge(Duration.ofDays(7)) // 쿠키 유효 기간 설정 (예: 7일)
//                .domain(frontDomain)
//                .build();
        ResponseCookie jwtCookie = ResponseCookie.from("JWT-TOKEN", newAccessToken)
                .httpOnly(true)     // 클라이언트에서 쿠키를 읽을 수 없도록 설정
                .secure(false)       // HTTPS에서만 전송하도록 설정 (개발 환경에서는 false)
                .path("/")          // 쿠키 경로 설정
                .maxAge(Duration.ofDays(7)) // 쿠키 유효 기간 설정 (예: 7일)
                .build();

        // JWT 쿠키를 응답 헤더에 추가
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
    }
}
