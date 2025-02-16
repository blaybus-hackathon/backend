package com.balybus.galaxy.login.serviceImpl;

import com.balybus.galaxy.login.dto.request.RefreshTokenDTO;
import com.balybus.galaxy.member.dto.request.MemberRequest;
import com.balybus.galaxy.member.dto.response.MemberResponse;

public interface LoginService {
    String renewAccessToken(RefreshTokenDTO refreshTokenDTO);
    String getRefreshToken();
    MemberResponse.SignInDto signIn(MemberRequest.SignInDto loginDto);
}
