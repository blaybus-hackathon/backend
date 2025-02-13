package com.balybus.galaxy.login.serviceImpl.service;

import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.helper.domain.Helper;
import com.balybus.galaxy.helper.domain.repository.HelperRepository;
import com.balybus.galaxy.login.domain.type.RoleType;
import com.balybus.galaxy.login.dto.request.SignUpDTO;
import com.balybus.galaxy.login.infrastructure.jwt.TokenProvider;
import com.balybus.galaxy.login.serviceImpl.LoginServiceImple;
import com.balybus.galaxy.member.domain.Member;
import com.balybus.galaxy.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.balybus.galaxy.global.exception.ExceptionCode.LOGIN_ID_EXIST;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService implements LoginServiceImple {

    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final HelperRepository helperRepository;


    public String renewAccessToken() {
        return tokenProvider.generateAccessToken("");
    }

    public String getRefreshToken() {
        return tokenProvider.refreshToken();
    }

    public void signUp(SignUpDTO signUpRequest) {
        // 1. email 중복성 검사
        if(memberRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new BadRequestException(LOGIN_ID_EXIST);
        }

        // 1.2 비밀번호 암호화
        String encryptedPassword = bCryptPasswordEncoder.encode(signUpRequest.getPassword());

        // 2. 기본 회원 정보 저장
        Member member = Member.builder()
                .email(signUpRequest.getEmail())
                .password(encryptedPassword)
                .userAuth(signUpRequest.getRoleType())
                .build();
        Member savedMember = memberRepository.save(member);

        // 3. 요양 보호사 정보 저장
        if(signUpRequest.getRoleType() == RoleType.MEMBER) {
            Helper helper = Helper.builder()
                    .member(savedMember)
                    .name(signUpRequest.getName())
                    .phone(signUpRequest.getPhone())
                    .addressDetail(signUpRequest.getAddressDetail())
                    .essentialCertNo(signUpRequest.getEssentialCertNo())
                    .carOwnYn(signUpRequest.isCarOwnYn())
                    .eduYn(signUpRequest.isEduYn())
                    .wage(signUpRequest.getWage())
                    .build();
            helperRepository.save(helper);
        }
    }

}
