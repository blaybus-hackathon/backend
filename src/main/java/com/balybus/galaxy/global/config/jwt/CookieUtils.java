package com.balybus.galaxy.global.config.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.ServerHttpRequest;
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
    @Value("${spring.profiles.active}")
    private String active;

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

    public void saveCookie(HttpServletRequest request, HttpServletResponse response, String newAccessToken) {
        // JWT 토큰을 HttpOnly 쿠키로 설정
        ResponseCookie jwtCookie;
        log.info("spring.profiles.active::"+active);
        String userAgent = request.getHeader("User-Agent");
        log.info("userAgent::{}", userAgent);

        if(active.equals("local")
                || (userAgent != null && userAgent.contains("PostmanRuntime"))){
            jwtCookie = ResponseCookie.from("JWT-TOKEN", newAccessToken)
                    .httpOnly(true)     // 클라이언트에서 쿠키를 읽을 수 없도록 설정
                    .secure(false)       // HTTPS에서만 전송하도록 설정 (개발 환경에서는 false)
                    .path("/")          // 쿠키 경로 설정
                    .maxAge(Duration.ofDays(7)) // 쿠키 유효 기간 설정 (예: 7일)
                    .sameSite("Strict")
                    .build();
        } else {
            jwtCookie = ResponseCookie.from("JWT-TOKEN", newAccessToken)
                    .httpOnly(true)     // 클라이언트에서 쿠키를 읽을 수 없도록 설정
                    .path("/")          // 쿠키 경로 설정
                    .maxAge(Duration.ofDays(7)) // 쿠키 유효 기간 설정 (예: 7일)
                    .sameSite("Strict") // SameSite 설정을 통해 CSRF 공격 방어
                    .secure(true)       // HTTPS에서만 전송하도록 설정 (개발 환경에서는 false)
                    .domain(frontDomain)
                    .build();
        }


        // JWT 쿠키를 응답 헤더에 추가
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
    }

    // SOCKET 통신 쿠키
    public String  getCookieForSocket(ServerHttpRequest request, String name) {
        String cookieHeader = request.getHeaders().getFirst(HttpHeaders.COOKIE);

        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split(";"); // 쿠키는 ;로 구분됨

            for (String cookie : cookies) {
                String[] cookiePair = cookie.trim().split("=");
                if (cookiePair.length == 2 && name.equals(cookiePair[0])) {
                    return cookiePair[1]; // JWT 쿠키 값 반환
                }
            }
        }

        return null; // 쿠키에서 JWT 토큰을 찾을 수 없으면 null 반환
    }
}
