package com.balybus.galaxy.login.classic.service.login;

import com.balybus.galaxy.careAssistant.domain.TblHelper;
import com.balybus.galaxy.careAssistant.repository.HelperRepository;
import com.balybus.galaxy.global.domain.tblAuthenticationMail.TblAuthenticationMailMsgEnum;
import com.balybus.galaxy.global.config.jwt.CookieUtils;
import com.balybus.galaxy.global.config.jwt.redis.TokenRedis;
import com.balybus.galaxy.global.config.jwt.redis.TokenRedisRepository;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.mail.ContentType;
import com.balybus.galaxy.global.utils.mail.SendMailRequest;
import com.balybus.galaxy.global.utils.mail.SendMailUtils;
import com.balybus.galaxy.global.utils.mail.dto.contents.ContentDto;
import com.balybus.galaxy.login.classic.domain.type.RoleType;
import com.balybus.galaxy.login.classic.dto.request.RefreshTokenDTO;
import com.balybus.galaxy.login.classic.infrastructure.jwt.TokenProvider;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.member.domain.type.LoginType;
import com.balybus.galaxy.member.dto.request.MemberRequest;
import com.balybus.galaxy.member.dto.response.MemberResponse;
import com.balybus.galaxy.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginServiceImpl implements LoginService {

    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SendMailUtils sendMailUtils;
    private final CookieUtils cookieUtils;

    private final MemberRepository memberRepository;
    private final TokenRedisRepository tokenRedisRepository;

    private final HelperRepository helperRepository;
    private final TblCenterManagerRepository centerManagerRepository;

    public String renewAccessToken(RefreshTokenDTO refreshTokenDTO) {
        return tokenProvider.renewAccessToken(refreshTokenDTO.getRefreshToken());
    }

    public String getRefreshToken() {
        return tokenProvider.refreshToken("");
    }


    /**
     * 로그인
     * @param signInDto MemberRequest.SignInDto
     * @return ApiResponse<MemberResponse.SignInDto>
     */
    @Override
    @Transactional
    public MemberResponse.SignInDto signIn(MemberRequest.SignInDto signInDto, HttpServletRequest request, HttpServletResponse response) {
        if (signInDto != null) {
            // 1. 사용자 조회
            Optional<TblUser> userOpt = memberRepository.findByEmail(signInDto.getUserId());
            if(userOpt.isPresent()) {
                TblUser login = userOpt.get();
                // 2. 비밀번호 일치하는지 확인
                if (bCryptPasswordEncoder.matches(signInDto.getUserPw(), login.getPassword())) {
                    // 3. 요양보호사/센터관리자 구분자 찾기
                    Long authSeq = null;
                    if(login.getUserAuth().equals(RoleType.MEMBER)){
                        Optional<TblHelper> helperOpt = helperRepository.findByUserId(login.getId());
                        if(helperOpt.isEmpty()) throw new BadRequestException(ExceptionCode.NOT_FOUND_HELPER);
                        authSeq = helperOpt.get().getId();
                    } else if(login.getUserAuth().equals(RoleType.MANAGER)){
                        Optional<TblCenterManager> cmOpt = centerManagerRepository.findByMember_Id(login.getId());
                        if(cmOpt.isEmpty()) throw new BadRequestException(ExceptionCode.NOT_FOUND_MANAGER);
                        authSeq = cmOpt.get().getId();
                    }

                    // 4. 1에서 찾은 데이터를 통해 JWT 생성 및 반환
                    String accessToken = tokenProvider.generateAccessToken(login.getEmail());
                    String refreshToken = tokenProvider.refreshToken(login.getEmail());
                    login.updateRefreshToken(refreshToken);

                    // 5. redis 에 토큰 저장
//                    tokenRedisRepository.save(new TokenRedis(login.getEmail(), accessToken, refreshToken));
                    cookieUtils.saveCookie(request, response, accessToken);

                    // 6. 조회 결과 전달
                    return MemberResponse.SignInDto.builder()
                            .chatSenderId(login.getId())
                            .email(login.getEmail())
                            .userAuth(login.getUserAuth())
                            .helperSeq(login.getUserAuth().equals(RoleType.MEMBER) ? authSeq : null)
                            .cmSeq(login.getUserAuth().equals(RoleType.MANAGER) ? authSeq : null)
                            .build();
                } else {
                    log.error("로그인 실패 : 아이디, 비밀번호 불일치, 사용자 ID {}", signInDto.getUserId());
                    throw new BadRequestException(ExceptionCode.LOGIN_FAIL); // 비밀번호 불일치 시 로그인 실패
                }
            } else {
                log.error("로그인 실패 : 존재하지 않는 사용자, 사용자 ID {}", signInDto.getUserId());
                throw new BadRequestException(ExceptionCode.LOGIN_FAIL);  // 존재하지 않는 사용자인 경우 Error
            }
        } else {
            log.error("로그인 실패 : LoginDto가 비어 있습니다.");
            throw new BadRequestException(ExceptionCode.LOGIN_FAIL); // LoginDto 값 비어 있을 때
        }
    }

    /**
     * 이메일 찾기
     * @param email String
     * @return MemberResponse.FindEmail
     */
    @Override
    public MemberResponse.FindEmail findEmail(String email) {
        //1. 이메일 등록 여부 조회
        Optional<TblUser> userOpt = memberRepository.findByEmail(email);
        TblAuthenticationMailMsgEnum state = userOpt.map(tblUser ->
                        (tblUser.getUserLoginType() == LoginType.KAKAO_LOGIN ?
                                TblAuthenticationMailMsgEnum.KAKAO_EMAIL
                                : TblAuthenticationMailMsgEnum.REGISTED_EMAIL))
                .orElse(TblAuthenticationMailMsgEnum.UNREGISTED_EMAIL);

        //2. 결과 반환
        return MemberResponse.FindEmail.builder()
                .code(state.getCode())
                .result(state.getMsg())
                .build();
    }

    /**
     * 비밀번호 찾기
     * @param email String
     * @return MemberResponse.FindPwd
     */
    @Override
    @Transactional
    public MemberResponse.FindPwd findPwd(String email) {
        //1. 조회 결과
        TblAuthenticationMailMsgEnum state = TblAuthenticationMailMsgEnum.UNREGISTED_EMAIL;

        //2. 이메일 등록 여부 조회
        Optional<TblUser> userOpt = memberRepository.findByEmail(email);
        if(userOpt.isPresent()){
            TblUser userEntity = userOpt.get();
            //2-1. SNS 로그인 여부 판독
            if(userEntity.getUserLoginType() == LoginType.KAKAO_LOGIN) {
                //2-2. 카카오 로그인의 경우, 카카오 로그인 사실 전달
                state = TblAuthenticationMailMsgEnum.KAKAO_EMAIL;
            } else {
                //2-3. SNS 로그인이 아닌 경우, 입력한 이메일로 임시비밀번호 전달
                state = TblAuthenticationMailMsgEnum.REGISTED_EMAIL;
                //2-3-1. 임시 비밀번호 생성
                String tempPwd = createTempPwd();
                //2-3-2. 임시 비밀번호 DB 저장
                userEntity.updatePwd(tempPwd);
                //2-3-3. 임시 비밀번호 메일 전송
                SendMailRequest request = SendMailRequest.builder()
                        .toMail(userEntity.getEmail())
                        .title("임시 비밀번호 발급")
                        .fromName("은하수 개발단")
                        .contentType(ContentType.TEMP_PWD)
                        .build();
                ContentDto<String> contentDto = new ContentDto<>(tempPwd);
                try {
                    sendMailUtils.sendMail(request, contentDto);
                } catch (UnsupportedEncodingException | MessagingException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        //5. 결과 반환
        return MemberResponse.FindPwd.builder()
                .code(state.getCode())
                .result(state.getMsg())
                .build();
    }

    /**
     * 임시 비밀번호 생성
     * @return String
     */
    private String createTempPwd(){
        int length = 12;
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

}
