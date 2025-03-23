package com.balybus.galaxy.login.serviceImpl.service;

import com.balybus.galaxy.domain.tblAuthenticationMail.TblAuthenticationMail;
import com.balybus.galaxy.domain.tblAuthenticationMail.TblAuthenticationMailMsgEnum;
import com.balybus.galaxy.domain.tblAuthenticationMail.TblAuthenticationMailRepository;
import com.balybus.galaxy.domain.tblCenter.TblCenter;
import com.balybus.galaxy.domain.tblCenter.TblCenterRepository;
import com.balybus.galaxy.domain.tblCenter.dto.CenterRequestDto;
import com.balybus.galaxy.domain.tblCenter.dto.CenterResponseDto;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.domain.tblCenterManager.dto.CenterManagerRequestDto;
import com.balybus.galaxy.domain.tblCenterManager.dto.CenterManagerResponseDto;
import com.balybus.galaxy.global.config.jwt.CookieUtils;
import com.balybus.galaxy.global.config.jwt.redis.TokenRedis;
import com.balybus.galaxy.global.config.jwt.redis.TokenRedisRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.mail.ContentType;
import com.balybus.galaxy.global.utils.mail.SendMailRequest;
import com.balybus.galaxy.global.utils.mail.SendMailUtils;
import com.balybus.galaxy.global.utils.mail.dto.contents.ContentDto;
import com.balybus.galaxy.global.utils.mail.dto.MailRequestDto;
import com.balybus.galaxy.global.utils.mail.dto.MailResponseDto;
import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.domain.TblHelperCert;
import com.balybus.galaxy.helper.repository.HelperCertRepository;
import com.balybus.galaxy.helper.repository.HelperRepository;
import com.balybus.galaxy.helper.serviceImpl.service.HelperServiceImpl;
import com.balybus.galaxy.login.domain.type.RoleType;
import com.balybus.galaxy.login.dto.request.HelperCertDTO;
import com.balybus.galaxy.login.dto.request.RefreshTokenDTO;
import com.balybus.galaxy.login.dto.request.SignUpDTO;
import com.balybus.galaxy.login.dto.response.TblHelperResponse;
import com.balybus.galaxy.login.infrastructure.jwt.TokenProvider;
import com.balybus.galaxy.login.serviceImpl.LoginService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.balybus.galaxy.global.exception.ExceptionCode.LOGIN_ID_EXIST;

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
    private final HelperRepository helperRepository;
    private final TblCenterRepository centerRepository;
    private final TblCenterManagerRepository centerManagerRepository;
    private final TblAuthenticationMailRepository authenticationMailRepository;
    private final TokenRedisRepository tokenRedisRepository;
    private final HelperCertRepository helperCertRepository;
    private final HelperServiceImpl helperServiceImpl;

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
                    // 3. 1에서 찾은 데이터를 통해 JWT 생성 및 반환
                    String accessToken = tokenProvider.generateAccessToken(login.getEmail());
                    String refreshToken = tokenProvider.refreshToken(login.getEmail());
                    login.updateRefreshToken(refreshToken);

                    // 4. redis 에 토큰 저장
                    tokenRedisRepository.save(new TokenRedis(login.getEmail(), accessToken, refreshToken));
                    cookieUtils.saveCookie(request, response, accessToken);

                    // 5. 조회 결과 전달
                    return MemberResponse.SignInDto.builder()
                            .chatSenderId(login.getId())
                            .email(login.getEmail())
                            .userAuth(login.getUserAuth())
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
     * 회원가입시 이메일 인증코드 발급 및 전송 API
     * @param dto MailRequestDto.AuthenticationMail
     * @return MailResponseDto.AuthenticationMail
     */
    @Override
    @Transactional
    public MailResponseDto.AuthenticationMail authenticationMail(MailRequestDto.AuthenticationMail dto) {
        //1. 등록된 이메일 인지 확인
        if(memberRepository.findByEmail(dto.getEmail()).isPresent())
            throw new BadRequestException(LOGIN_ID_EXIST);

        //2. 인증코드 생성
        String code = createAuthenticationCode();

        //3. 인증코드 이메일 전송
        SendMailRequest request = SendMailRequest.builder()
                .toMail(dto.getEmail())
                .title("이메일 인증")
                .fromName("은하수 개발단")
                .contentType(ContentType.AUTHENTICATION)
                .build();
        ContentDto<String> contentDto = new ContentDto<>(code);
        try {
            sendMailUtils.sendMail(request, contentDto);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }

        //4. 인증코드 저장
        Optional<TblAuthenticationMail> entityOpt = authenticationMailRepository.findByEmail(dto.getEmail());
        TblAuthenticationMail entity = entityOpt.orElseGet(() -> TblAuthenticationMail.builder()
                                                                        .email(dto.getEmail())
                                                                        .build());
        entity.updateCode(code);
        Long amSeq = authenticationMailRepository.save(entity).getId();

        log.info("save AuthenticationMail");
        return MailResponseDto.AuthenticationMail.builder().mailSeq(amSeq).build();
    }

    /**
     * 회원가입시 이메일 인증코드 일치 여부 확인 API
     * @param dto MailRequestDto.CheckAuthenticationCode
     * @return MailResponseDto.CheckAuthenticationCode
     */
    @Override
    @Transactional
    public MailResponseDto.CheckAuthenticationCode checkAuthenticationCode(MailRequestDto.CheckAuthenticationCode dto) {
        //1. 전송테이블에서 seq, 이메일, 인증코드로 데이터 조회
        Optional<TblAuthenticationMail> mailOpt = authenticationMailRepository.findByIdAndEmailAndCode(dto.getMailSeq(), dto.getEmail(), dto.getCode());
        boolean checker = mailOpt.isPresent();

        //2. 조회 결과가 있는 경우, 메일 데이터 삭제
        if(checker) authenticationMailRepository.delete(mailOpt.get());

        //3. 조회된 데이터가 존재 여부 반환
        return MailResponseDto.CheckAuthenticationCode.builder().checker(checker).build();
    }

    private String createAuthenticationCode(){
        int length = 5;
        String characters = "1234567890";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }


    /**
     * 이메일 유효셩 검사 및 아이디 비밀번호 등록
     * @param email String:이메일(아이디)
     * @param pw String:비밀번호
     * @param roleType RoleTyp(Enum):권한
     * @return TblUser
     */
    private TblUser signUpLogin(String email, String pw, RoleType roleType, LoginType loginType) {
        // 1. email 중복성 검사
        if(memberRepository.findByEmail(email).isPresent())
            throw new BadRequestException(LOGIN_ID_EXIST);

        // 2. 비밀번호 암호화
        String encryptedPassword = bCryptPasswordEncoder.encode(pw);

        // 3. 기본 회원 정보 저장
        TblUser member = TblUser.builder()
                .email(email)
                .password(encryptedPassword)
                .userAuth(roleType)
                .userLoginType(loginType)
                .build();

        return memberRepository.save(member);
    }

    /**
     * 요양보호사 회원가입
     * @param signUpRequest SignUpDTO
     * @return TblHelperResponse
     */
    public List<HelperCertDTO> signUp(SignUpDTO signUpRequest) {
        // 1.이메일 유효셩 검사 및 아이디 비밀번호 등록
        TblUser savedMember = signUpLogin(signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getRoleType(), LoginType.DOLBOM_LOGIN);

        TblHelper helper = null;

        // 2. 요양 보호사 정보 저장
        if(signUpRequest.getRoleType() == RoleType.MEMBER) {
            helper = TblHelper.builder()
                    .user(savedMember)
                    .name(signUpRequest.getName())
                    .phone(signUpRequest.getPhone())
                    .gender(signUpRequest.getGender())
                    .birthday(signUpRequest.getBirthday())
                    .addressDetail(signUpRequest.getAddressDetail())
                    .carOwnYn(signUpRequest.isCarOwnYn())
                    .eduYn(signUpRequest.isEduYn())
                    .build();
            helperRepository.save(helper);
        }

        // 3. 요양 보호사 자격증 별로 따로 정보 저장 + 요양 보호사 자격증 진위 여부 검사 -> Q-net
        List<HelperCertDTO> invalidCertList = new ArrayList<>();

        if(helper != null) {
            List<HelperCertDTO> certList = new ArrayList<>();
            certList.add(signUpRequest.getEssentialCertNo());
            certList.add(signUpRequest.getCareCertNo());
            certList.add(signUpRequest.getNurseCertNo());
            certList.add(signUpRequest.getPostPartumCertNo());
            certList.add(signUpRequest.getHelperOtherCerts());

            String name = signUpRequest.getName();
            String birthday = signUpRequest.getBirthday();
            for(HelperCertDTO cert : certList) {
                String checkCertVerify = helperServiceImpl.checkCertificate(
                        name,
                        birthday,
                        cert.getCertNum(),
                        String.valueOf(cert.getCertDateIssue()),
                        String.valueOf(cert.getCertSerialNum())
                );
                if(checkCertVerify.equals("NOT_FOUND") ||
                    checkCertVerify.equals("UNKNOWN")
                ) {
                    invalidCertList.add(cert);
                    continue;
                }
                helperCertRepository.save(makeCertTbl(cert.getCertName(),
                        helper,
                        cert.getCertNum(),
                        cert.getCertDateIssue(),
                        cert.getCertSerialNum()));
            }
        }

        return invalidCertList;
    }

    public TblHelperCert makeCertTbl(String name,
                                     TblHelper tblHelper,
                                     String certNum,
                                     Integer certDateIssue,
                                     Integer certSerialNum) {
        return TblHelperCert.builder()
                .tblHelper(tblHelper)
                .certName(name)
                .certNum(certNum)
                .certDateIssue(certDateIssue)
                .certSerialNum(certSerialNum)
                .build();
    }

    /**
     * 센터 등록
     * @param dto CenterRequestDto.RegisterCenter
     * @return CenterDto
     */
    @Override
    @Transactional
    public CenterResponseDto.RegisterCenter registerCenter(CenterRequestDto.RegisterCenter dto) {
        // 1. 센터 정보 조회
        Optional<TblCenter> centerOpt = centerRepository.findByCenterAddress(dto.getAddress());
        if(centerOpt.isPresent())
            throw new BadRequestException(ExceptionCode.CENTER_EXIST);

        //2. 센터 코드 생성
        String centerCode = createCenterCode();

        //3. 센터 정보 등록
        Long centerSeq = centerRepository.save(dto.toEntity(centerCode)).getId();
        return CenterResponseDto.RegisterCenter.builder()
                .centerSeq(centerSeq)
                .build();
    }

    // 센터 코드 생성
    private String createCenterCode(){
        int totalLen = 6;
        int charLen = 2;
        Random random = new Random();
        String centerCode;

        while (true) {
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String nums = "1234567890";
            StringBuilder sb = new StringBuilder(totalLen);
            for (int i = 0; i < totalLen; i++) {
                if(i < charLen){
                    int index = random.nextInt(characters.length());
                    sb.append(characters.charAt(index));
                } else {
                    int index = random.nextInt(nums.length());
                    sb.append(nums.charAt(index));
                }
            }
            centerCode = sb.toString();
            Optional<TblCenter> centerOpt = centerRepository.findByCenterCode(centerCode);
            if(centerOpt.isEmpty()) break;
        }

        return centerCode;
    }

    /**
     * 관리자 회원가입
     * @param dto CenterManagerRequestDto
     * @return CenterManagerResponseDto
     */
    @Transactional
    public CenterManagerResponseDto.SignUpManager signUpManager(CenterManagerRequestDto.SignUpManager dto) {
        // 1. 센터 정보 확인
        Optional<TblCenter> centerOpt = centerRepository.findById(dto.getCenterSeq());
        if(centerOpt.isEmpty())
            throw new BadRequestException(ExceptionCode.CENTER_NOT_FOUND);

        // 2. 이메일 유효셩 검사 및 관리자 아이디 비밀번호 등록
        TblUser savedMember = signUpLogin(dto.getEmail(), dto.getPassword(), RoleType.MANAGER, LoginType.DOLBOM_LOGIN);

        // 3. 관리자 정보 등록
        Long cmSeq = centerManagerRepository.save(
                TblCenterManager.builder()
                        .member(savedMember)            // 유저
                        .center(centerOpt.get())        // 센터
                        .cmPosition(dto.getPosition())  // 직책
                        .cmName(dto.getName())          // 직원명
                        .build()).getId();

        // 4. ResponseDTO 반환
        return CenterManagerResponseDto.SignUpManager.builder()
                .cmSeq(cmSeq)  // 관리자 구분자
                .build();
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
