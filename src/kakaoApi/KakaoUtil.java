package com.balybus.galaxy.kakaoApi;

import com.balybus.galaxy.kakaoApi.DTO.KakaoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoUtil {

    @Value("${spring.kakao.auth.client}")
    private String client;

    @Value("${spring.kakao.auth.redirect}")
    private String redirect;

    private final RestTemplate restTemplate;

    public KakaoDTO.OAuthToken requestToken(String accessCode) {
        // 기존 requestToken 로직과 동일
        return null;
    }

    public KakaoDTO.KakaoProfile requestProfile(KakaoDTO.OAuthToken token) {
        // 카카오 프로필 API 요청 로직 추가
        return null;
    }
}
