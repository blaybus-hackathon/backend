package com.balybus.galaxy.oauth.client;

import com.balybus.galaxy.oauth.dto.request.KakaoUser;
import com.balybus.galaxy.oauth.dto.response.OauthToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(name = "kakao-auth-api", url = "https://kauth.kakao.com") // URL을 kauth.kakao.com 으로 변경
public interface KakaoApiToken {

    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
    OauthToken getToken(@RequestParam("grant_type") String grantType,
                        @RequestParam("client_id") String clientId,
                        @RequestParam("redirect_uri") String redirectUri,
                        @RequestParam("code") String code);
}