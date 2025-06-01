package com.balybus.galaxy.login.classic.service.login;

import com.balybus.galaxy.global.domain.tblCenter.dto.CenterRequestDto;
import com.balybus.galaxy.global.domain.tblCenter.dto.CenterResponseDto;
import com.balybus.galaxy.global.domain.tblCenterManager.dto.CenterManagerRequestDto;
import com.balybus.galaxy.global.domain.tblCenterManager.dto.CenterManagerResponseDto;
import com.balybus.galaxy.global.utils.mail.dto.MailRequestDto;
import com.balybus.galaxy.global.utils.mail.dto.MailResponseDto;
import com.balybus.galaxy.login.classic.dto.request.RefreshTokenDTO;
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
    CenterResponseDto.GetCenterList getCenterList(CenterRequestDto.GetCenterList centerDto);
    CenterResponseDto.RegisterCenter registerCenter(CenterRequestDto.RegisterCenter centerDto);
    CenterManagerResponseDto.SignUpManager signUpManager(CenterManagerRequestDto.SignUpManager centerDto);
    MemberResponse.FindEmail findEmail(String email);
    MemberResponse.FindPwd findPwd(String email);

}
