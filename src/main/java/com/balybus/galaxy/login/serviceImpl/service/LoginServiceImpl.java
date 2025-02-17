package com.balybus.galaxy.login.serviceImpl.service;

import com.balybus.galaxy.address.repository.TblAddressFirstRepository;
import com.balybus.galaxy.address.repository.TblAddressSecondRepository;
import com.balybus.galaxy.address.repository.TblAddressThirdRepository;
import com.balybus.galaxy.domain.tblCenter.TblCenter;
import com.balybus.galaxy.domain.tblCenter.TblCenterRepository;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.domain.tblCenterManager.dto.CenterManagerRequestDto;
import com.balybus.galaxy.domain.tblCenterManager.dto.CenterManagerResponseDto;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.balybus.galaxy.global.exception.ExceptionCode.LOGIN_ID_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginServiceImpl implements LoginService {

    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final HelperRepository helperRepository;

    private final TblCenterRepository centerRepository;
    private final TblCenterManagerRepository centerManagerRepository;

    private final TblAddressFirstRepository tblAddressFirstRepository;
    private final TblAddressSecondRepository tblAddressSecondRepository;
    private final TblAddressThirdRepository tblAddressThirdRepository;

    public String renewAccessToken(RefreshTokenDTO refreshTokenDTO) {
        return tokenProvider.renewAccessToken(refreshTokenDTO.getRefreshToken());
    }

    public String getRefreshToken() {
        return tokenProvider.refreshToken("");
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
                    .socialCertNo(signUpRequest.getSocialCertNo())
                    .nurseCertNo(signUpRequest.getNurseCertNo())
                    .carOwnYn(signUpRequest.isCarOwnYn())
                    .eduYn(signUpRequest.isEduYn())
                    .wage(signUpRequest.getWage())
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
     * 로그인
     * @param signInDto MemberRequest.SignInDto
     * @return ApiResponse<MemberResponse.SignInDto>
     */
    @Override
    @Transactional
    public MemberResponse.SignInDto signIn(MemberRequest.SignInDto signInDto) {
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

                    // 4. 조회 결과 전달
                    return MemberResponse.SignInDto.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
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
