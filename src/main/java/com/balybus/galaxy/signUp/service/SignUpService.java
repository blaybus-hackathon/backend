package com.balybus.galaxy.signUp.service;

import com.balybus.galaxy.global.domain.tblCenter.dto.CenterRequestDto;
import com.balybus.galaxy.global.domain.tblCenter.dto.CenterResponseDto;
import com.balybus.galaxy.global.domain.tblCenterManager.dto.CenterManagerRequestDto;
import com.balybus.galaxy.global.domain.tblCenterManager.dto.CenterManagerResponseDto;
import com.balybus.galaxy.global.utils.mail.dto.MailRequestDto;
import com.balybus.galaxy.global.utils.mail.dto.MailResponseDto;
import com.balybus.galaxy.login.classic.dto.request.SignUpDTO;
import com.balybus.galaxy.login.classic.dto.response.HelperSignUpResponseDto;

import java.util.List;

public interface SignUpService {
    MailResponseDto.AuthenticationMail authenticationMail(MailRequestDto.AuthenticationMail dto);
    MailResponseDto.CheckAuthenticationCode checkAuthenticationCode(MailRequestDto.CheckAuthenticationCode dto);
    HelperSignUpResponseDto signUpHelper(SignUpDTO signUpRequest);
    CenterResponseDto.GetCenterList getCenterList(CenterRequestDto.GetCenterList centerDto);
    CenterResponseDto.RegisterCenter registerCenter(CenterRequestDto.RegisterCenter centerDto);
    CenterManagerResponseDto.SignUpManager signUpManager(CenterManagerRequestDto.SignUpManager centerDto);
}
