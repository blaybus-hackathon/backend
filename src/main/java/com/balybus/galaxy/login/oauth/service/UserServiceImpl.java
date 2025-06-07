package com.balybus.galaxy.login.oauth.service;

import com.balybus.galaxy.login.oauth.dto.request.KakaoRequest;
import com.balybus.galaxy.login.oauth.dto.request.KakaoUser;
import com.balybus.galaxy.login.oauth.dto.response.KakaoResponse;
import com.balybus.galaxy.login.oauth.dto.response.OauthToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserServiceImpl {
    public KakaoResponse kakaoLogin(KakaoRequest code, HttpServletRequest request, HttpServletResponse response);

    private KakaoUser getUserInfo(String accessToken) {
        return null;
    }

    private OauthToken getAccessToken(String code) {
        return null;
    }

    private KakaoUser getUserInfoRT(String accessToken) {
        return null;
    }
    private OauthToken getAccessTokenRT(String code) {
        return null;
    }

}
