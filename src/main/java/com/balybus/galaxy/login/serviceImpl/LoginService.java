package com.balybus.galaxy.login.serviceImpl;

import com.balybus.galaxy.global.response.CustomApiResponse;
import com.balybus.galaxy.member.dto.request.MemberRequest;
import com.balybus.galaxy.member.dto.response.MemberResponse;

public interface LoginService {
    public abstract String renewAccessToken();
    public String getRefreshToken();
    MemberResponse.SignInDto signIn(MemberRequest.SignInDto loginDto);
}
