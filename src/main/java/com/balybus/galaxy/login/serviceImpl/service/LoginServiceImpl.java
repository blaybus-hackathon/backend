package com.balybus.galaxy.login.serviceImpl.service;

import com.balybus.galaxy.domain.tblAuthenticationMail.TblAuthenticationMail;
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
import com.balybus.galaxy.helper.repositoryImpl.HelperRepository;
import com.balybus.galaxy.login.domain.type.RoleType;
import com.balybus.galaxy.login.dto.request.RefreshTokenDTO;
import com.balybus.galaxy.login.dto.request.SignUpDTO;
import com.balybus.galaxy.login.dto.response.TblHelperResponse;
import com.balybus.galaxy.login.infrastructure.jwt.TokenProvider;
import com.balybus.galaxy.login.serviceImpl.LoginService;
import com.balybus.galaxy.member.domain.TblUser;
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
    private TblUser signUpLogin(String email, String pw, RoleType roleType) {
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
                .build();

        return memberRepository.save(member);
    }

    /**
     * 요양보호사 회원가입
     * @param signUpRequest SignUpDTO
     * @return TblHelperResponse
     */
    public TblHelperResponse signUp(SignUpDTO signUpRequest) {
        // 1.이메일 유효셩 검사 및 아이디 비밀번호 등록
        TblUser savedMember = signUpLogin(signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getRoleType());

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
                    .essentialCertNo(signUpRequest.getEssentialCertNo())
                    .careCertNo(signUpRequest.getCareCertNo())
                    .nurseCertNo(signUpRequest.getNurseCertNo())
                    .postPartumCertNo(signUpRequest.getPostPartumCertNo())
                    .helperOtherCerts(signUpRequest.getHelperOtherCerts())
                    .carOwnYn(signUpRequest.isCarOwnYn())
                    .eduYn(signUpRequest.isEduYn())
                    .build();
            helperRepository.save(helper);
        }

        return TblHelperResponse.builder()
                .name(helper.getName())
                .phone(helper.getPhone())
                .addressDetail(helper.getAddressDetail())
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
    public CenterManagerResponseDto registerManager(CenterManagerRequestDto dto) {
        // 1. 센터 정보 확인
        Optional<TblCenter> centerOpt = centerRepository.findById(dto.getCenterSeq());
        if(centerOpt.isEmpty())
            throw new BadRequestException(ExceptionCode.CENTER_NOT_FOUND);

        // 2. 이메일 유효셩 검사 및 관리자 아이디 비밀번호 등록
        TblUser savedMember = signUpLogin(dto.getEmail(), dto.getPassword(), RoleType.MANAGER);

        // 3. 관리자 정보 등록
        TblCenterManager manager = centerManagerRepository.save(
                TblCenterManager.builder()
                        .member(savedMember)            // 유저
                        .center(centerOpt.get())        // 센터
                        .cmPosition(dto.getPosition())  // 직책
                        .cmName(dto.getName())          // 직원명
                        .build());

        // 4. ResponseDTO 반환
        return CenterManagerResponseDto.builder()
                .id(manager.getId())  // 관리자 구분자
                .userSeq(manager.getMember().getId())  // 유저 구분자
                .centerSeq(manager.getCenter().getId())  // 센터 구분자
                .position(manager.getCmPosition())  // 직책
                .name(manager.getCmName())  // 직원명
                .build();
    }

}
