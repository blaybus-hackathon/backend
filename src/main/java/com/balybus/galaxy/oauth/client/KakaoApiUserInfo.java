package com.balybus.galaxy.oauth.client;

import com.balybus.galaxy.oauth.dto.request.KakaoUser;
import com.balybus.galaxy.oauth.dto.request.KakaoUserFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-userinfo-api", url = "https://kapi.kakao.com")
public interface KakaoApiUserInfo {

    @GetMapping(value = "/v2/user/me", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    KakaoUserFeign getUserInfo(@RequestHeader("Authorization") String authorization);
}