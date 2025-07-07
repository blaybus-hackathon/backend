package com.balybus.galaxy.signUp.service;

import com.balybus.galaxy.careAssistant.domain.TblHelper;
import com.balybus.galaxy.careAssistant.domain.TblHelperCert;
import com.balybus.galaxy.careAssistant.repository.HelperCertRepository;
import com.balybus.galaxy.careAssistant.repository.HelperRepository;
import com.balybus.galaxy.careAssistant.service.HelperServiceImpl;
import com.balybus.galaxy.global.domain.tblAuthenticationMail.TblAuthenticationMail;
import com.balybus.galaxy.global.domain.tblAuthenticationMail.TblAuthenticationMailRepository;
import com.balybus.galaxy.global.domain.tblCenter.TblCenter;
import com.balybus.galaxy.global.domain.tblCenter.TblCenterRepository;
import com.balybus.galaxy.global.domain.tblCenter.dto.CenterRequestDto;
import com.balybus.galaxy.global.domain.tblCenter.dto.CenterResponseDto;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.global.domain.tblCenterManager.dto.CenterManagerRequestDto;
import com.balybus.galaxy.global.domain.tblCenterManager.dto.CenterManagerResponseDto;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.mail.ContentType;
import com.balybus.galaxy.global.utils.mail.SendMailRequest;
import com.balybus.galaxy.global.utils.mail.SendMailUtils;
import com.balybus.galaxy.global.utils.mail.dto.MailRequestDto;
import com.balybus.galaxy.global.utils.mail.dto.MailResponseDto;
import com.balybus.galaxy.global.utils.mail.dto.contents.ContentDto;
import com.balybus.galaxy.login.classic.domain.type.RoleType;
import com.balybus.galaxy.login.classic.dto.request.HelperCertDTO;
import com.balybus.galaxy.login.classic.dto.request.SignUpDTO;
import com.balybus.galaxy.login.classic.dto.response.HelperSignUpResponseDto;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.member.domain.type.LoginType;
import com.balybus.galaxy.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SignUpServiceImpl implements SignUpService{

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SendMailUtils sendMailUtils;

    private final MemberRepository memberRepository;
    private final HelperRepository helperRepository;
    private final TblCenterRepository centerRepository;
    private final TblCenterManagerRepository centerManagerRepository;
    private final TblAuthenticationMailRepository authenticationMailRepository;
    private final HelperCertRepository helperCertRepository;
    private final HelperServiceImpl helperServiceImpl;

    /**
     * 이메일 인증 코드 생성
     * @return String : 5자리 인증코드
     */
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
     * 회원가입시 이메일 인증코드 발급 및 전송 API
     * @param dto MailRequestDto.AuthenticationMail
     * @return MailResponseDto.AuthenticationMail
     */
    @Override
    @Transactional
    public MailResponseDto.AuthenticationMail authenticationMail(MailRequestDto.AuthenticationMail dto) {
        //1. 등록된 이메일 인지 확인
        if(memberRepository.findByEmail(dto.getEmail()).isPresent())
            throw new BadRequestException(ExceptionCode.LOGIN_ID_EXIST);


        //2. 10분에 한번 요청 가능하도록 제한
        Optional<TblAuthenticationMail> entityOpt = authenticationMailRepository.findByEmail(dto.getEmail());
        if(entityOpt.isPresent() && entityOpt.get().getUpdateDatetime().plusMinutes(10L).isAfter(LocalDateTime.now()))
            throw new BadRequestException(ExceptionCode.TOO_MUCH_MAIL_REQUEST);

        //3. 인증코드 생성
        String code = createAuthenticationCode();

        //4. 인증코드 이메일 전송
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

        //5. 인증코드 저장
        TblAuthenticationMail entity = entityOpt.orElseGet(() -> TblAuthenticationMail.builder()
                .email(dto.getEmail())
                .certificationYn(false)
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
        String msg = checker ? "SUCCESS":ExceptionCode.INVALID_REQUEST.getMessage();

        //2. 조회 결과가 있는 경우, 인증코드 승인 처리
        if(checker){
            checker = mailOpt.get().getUpdateDatetime().plusMinutes(10L).isAfter(LocalDateTime .now());
            if(checker) mailOpt.get().approve();
            else msg = ExceptionCode.AFTER_10_MINUTES.getMessage();
        }

        //3. 조회된 데이터가 존재 여부 반환
        return MailResponseDto.CheckAuthenticationCode.builder()
                .checker(checker)
                .msg(msg)
                .build();
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
            throw new BadRequestException(ExceptionCode.LOGIN_ID_EXIST);

        // 2. 이메일 인증 여부 확인
        Optional<TblAuthenticationMail> mailOpt = authenticationMailRepository.findByEmail(email);
        if(mailOpt.isEmpty() || !mailOpt.get().isCertificationYn())
            throw new BadRequestException(ExceptionCode.REQUIRED_EMAIL_VERIFICATION);

        // 3. 비밀번호 암호화
        String encryptedPassword = bCryptPasswordEncoder.encode(pw);

        // 4. 기본 회원 정보 저장
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
     * @param signUpRequest SignUpRequestDto.SignUpDTO
     * @return TblHelperResponse
     */
    @Override
    public HelperSignUpResponseDto signUpHelper(SignUpDTO signUpRequest) {
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
            if(signUpRequest.getEssentialCertNo() != null) {
                certList.add(signUpRequest.getEssentialCertNo());
            }
            if(signUpRequest.getCareCertNo() != null) {
                certList.add(signUpRequest.getCareCertNo());
            }
            if(signUpRequest.getNurseCertNo() != null) {
                certList.add(signUpRequest.getNurseCertNo());
            }
            if(signUpRequest.getPostPartumCertNo() != null) {
                certList.add(signUpRequest.getPostPartumCertNo());
            }
            if(signUpRequest.getHelperOtherCerts() != null) {
                certList.add(signUpRequest.getHelperOtherCerts());
            }


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
//                helperCertRepository.save(makeCertTbl(cert.getCertName(),
//                        helper,
//                        cert.getCertNum(),
//                        cert.getCertDateIssue(),
//                        cert.getCertSerialNum()));
                helperCertRepository.save(
                        TblHelperCert.builder()
                                .tblHelper(helper)
                                .certName(cert.getCertName())
                                .certNum(cert.getCertNum())
                                .certDateIssue(cert.getCertDateIssue())
                                .certSerialNum(cert.getCertSerialNum())
                                .build());
            }
        }

        return HelperSignUpResponseDto.builder()
                .helperSeq(helper.getId())
                .invalidCertList(invalidCertList)
                .build();
    }

//    public TblHelperCert makeCertTbl(String name,
//                                     TblHelper tblHelper,
//                                     String certNum,
//                                     Integer certDateIssue,
//                                     Integer certSerialNum) {
//        return TblHelperCert.builder()
//                .tblHelper(tblHelper)
//                .certName(name)
//                .certNum(certNum)
//                .certDateIssue(certDateIssue)
//                .certSerialNum(certSerialNum)
//                .build();
//    }

    /**
     * 센터 조회
     * @param dto CenterRequestDto.GetCenterList
     * @return CenterResponseDto.GetCenterList
     */
    @Override
    @Transactional
    public CenterResponseDto.GetCenterList getCenterList(CenterRequestDto.GetCenterList dto) {
        // 1. 센터 조회
        Pageable page = PageRequest.of(
                dto.getPageNo()==null ? 0 : dto.getPageNo()
                , dto.getPageSize()==null ? 10 : dto.getPageSize()
                , Sort.by(Sort.Order.asc("centerName"), Sort.Order.desc("id")));
        Page<TblCenter> listPage = centerRepository.findByCenterNameContainsOrCenterAddressContainsOrCenterCode(dto.getSearchName(), dto.getSearchName(), dto.getSearchName(), page);
        List<TblCenter> centerEntityList = listPage.getContent();

        //2. dto 전환
        List<CenterResponseDto.GetCenterListInfo> resultList = new ArrayList<>();
        for (TblCenter entity : centerEntityList) {
            resultList.add(
                    CenterResponseDto.GetCenterListInfo.builder()
                            .centerSeq(entity.getId())
                            .centerName(entity.getCenterName())
                            .centerAddress(entity.getCenterAddress())
                            .build());
        }

        //3. 센터 리스트 반환
        return CenterResponseDto.GetCenterList.builder()
                .totalPage(listPage.getTotalPages())
                .totalEle(listPage.getTotalElements())
                .hasNext(listPage.hasNext())
                .list(resultList)
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
}
