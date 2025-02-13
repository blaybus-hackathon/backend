package com.balybus.galaxy.login.serviceImpl;

import com.balybus.galaxy.global.response.ApiResponse;
import com.balybus.galaxy.member.dto.request.MemberRequest;

public interface LoginService {
    public abstract String renewAccessToken();
    public String getRefreshToken();
    ApiResponse<?> signIn(MemberRequest.SignInDto loginDto);
}
