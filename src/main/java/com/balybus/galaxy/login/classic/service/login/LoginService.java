package com.balybus.galaxy.login.classic.service.login;

import com.balybus.galaxy.login.classic.dto.request.RefreshTokenDTO;
import com.balybus.galaxy.member.dto.request.MemberRequest;
import com.balybus.galaxy.member.dto.response.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {
    String renewAccessToken(RefreshTokenDTO refreshTokenDTO);
    String getRefreshToken();
    MemberResponse.SignInDto signIn(MemberRequest.SignInDto loginDto, HttpServletRequest request, HttpServletResponse response);
    MemberResponse.FindEmail findEmail(String email);
    MemberResponse.FindPwd findPwd(String email);

}
