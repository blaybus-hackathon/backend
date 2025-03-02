package com.balybus.galaxy.kakaoApi;

public class SecurityConstants {  // 클래스 내부에 선언

    public static final String[] ALLOWED_URLS = {
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/api/v1/posts/**",
            "/api/v1/replies/**",
            "/login",
            "/auth/login/kakao/**"
    };
}