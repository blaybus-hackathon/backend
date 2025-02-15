package com.balybus.galaxy.login.serviceImpl;

import com.balybus.galaxy.global.response.ApiResponse;
import com.balybus.galaxy.login.dto.request.RefreshTokenDTO;
import com.balybus.galaxy.member.dto.request.MemberRequest;

public interface LoginService {
    public abstract String renewAccessToken(RefreshTokenDTO refreshTokenDTO);
    public String getRefreshToken();
    ApiResponse<?> signIn(MemberRequest.SignInDto loginDto);
}
