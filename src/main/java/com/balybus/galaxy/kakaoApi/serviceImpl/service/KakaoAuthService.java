package com.balybus.galaxy.kakaoApi.serviceImpl.service;

import com.balybus.galaxy.kakaoApi.DTO.KakaoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    @Value("${kakao.client-id}")
    private String client;

    @Value("${kakao.redirect-uri}")
    private String redirect;

    private final RestTemplate restTemplate;

    public KakaoDTO.OAuthToken requestToken(String accessCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client);
        params.add("redirect_uri", redirect);
        params.add("code", accessCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

        // JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoDTO.OAuthToken oAuthToken = null;

        try {
            oAuthToken = objectMapper.readValue(response.getBody(), KakaoDTO.OAuthToken.class);
            log.info("oAuthToken : {}", oAuthToken.getAccess_token());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 오류", e);
        }

        return oAuthToken;
    }
}
