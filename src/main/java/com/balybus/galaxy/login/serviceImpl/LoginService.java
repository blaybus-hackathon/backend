package com.balybus.galaxy.login.serviceImpl;

import com.balybus.galaxy.domain.tblCenter.dto.CenterRequestDto;
import com.balybus.galaxy.domain.tblCenter.dto.CenterResponseDto;
import com.balybus.galaxy.global.utils.mail.dto.MailRequestDto;
import com.balybus.galaxy.global.utils.mail.dto.MailResponseDto;
import com.balybus.galaxy.login.dto.request.RefreshTokenDTO;
import com.balybus.galaxy.member.dto.request.MemberRequest;
import com.balybus.galaxy.member.dto.response.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {
    String renewAccessToken(RefreshTokenDTO refreshTokenDTO);
    String getRefreshToken();
    MemberResponse.SignInDto signIn(MemberRequest.SignInDto loginDto, HttpServletRequest request, HttpServletResponse response);
    MailResponseDto.AuthenticationMail authenticationMail(MailRequestDto.AuthenticationMail dto);
    MailResponseDto.CheckAuthenticationCode checkAuthenticationCode(MailRequestDto.CheckAuthenticationCode dto);
    CenterResponseDto.RegisterCenter registerCenter(CenterRequestDto.RegisterCenter centerDto);

}
