package com.balybus.galaxy.careAssistant.service;

import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;
import com.balybus.galaxy.global.domain.tblAddressSecond.TblAddressSecond;
import com.balybus.galaxy.global.domain.tblAddressThird.TblAddressThird;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.address.repository.TblAddressFirstRepository;
import com.balybus.galaxy.global.utils.address.repository.TblAddressSecondRepository;
import com.balybus.galaxy.global.utils.address.repository.TblAddressThirdRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.careAssistant.domain.*;
import com.balybus.galaxy.careAssistant.dto.request.*;
import com.balybus.galaxy.careAssistant.dto.response.*;
import com.balybus.galaxy.careAssistant.repository.*;
import com.balybus.galaxy.login.classic.dto.request.HelperCertDTO;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.balybus.galaxy.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HelperServiceImpl implements HelperService {

    private final HelperRepository helperRepository;
    private final MemberRepository memberRepository;

    private final HelperCertRepository helperCertRepository;
    private final HelperWorkLocationRepository helperWorkLocationRepository;
    private final HelperWorkTimeRepository helperWorkTimeRepository;
    private final HelperExperienceRepository helperExperienceRepository;

    private final TblAddressFirstRepository tblAddressFirstRepository;
    private final TblAddressSecondRepository tblAddressSecondRepository;
    private final TblAddressThirdRepository tblAddressThirdRepository;

    @Override
    public HelperResponse getAllHelperInfo(UserDetails userDetails) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        List<TblHelperCert> certificates = helperCertRepository.findByTblHelperId(tblHelper.getId());
        List<HelperCertDTO> certDTOList = new ArrayList<>();

        for(TblHelperCert tblHelperCert : certificates) {
            certDTOList.add(HelperCertDTO.builder()
                    .certName(tblHelperCert.getCertName())
                    .certNum(tblHelperCert.getCertNum())
                    .certDateIssue(tblHelperCert.getCertDateIssue())
                    .certSerialNum(tblHelperCert.getCertSerialNum())
                    .build());
        }

        return HelperResponse.builder()
                .id(tblHelper.getId())
                .userEmail(tblUser.getEmail())
                .name(tblHelper.getName())
                .phone(tblHelper.getPhone())
                .addressDetail(tblHelper.getAddressDetail())
                .certificates(certDTOList)
                .carOwnYn(tblHelper.isCarOwnYn())
                .eduYn(tblHelper.isEduYn())
                .wage(tblHelper.getWage())
                .wageState(tblHelper.getWageState())
                .introduce(tblHelper.getIntroduce())
                .careExperience(tblHelper.getIs_experienced())
                .build();
    }

    @Override
    @Transactional
    public Map<String, Object> updateProfile(UserDetails userDetails, HelperProfileDTO helperProfileDTO) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        Map<String, Object> result = new HashMap<>();
        Map<String, String> certificateResults = new HashMap<>();
        
        // null이 아닌 필드만 업데이트 (부분 업데이트 지원)
        boolean profileUpdated = false;
        if (helperProfileDTO.getIntroduce() != null) {
            tblHelper.setIntroduce(helperProfileDTO.getIntroduce());
            profileUpdated = true;
        }
        if (helperProfileDTO.getCareExperience() != null) {
            tblHelper.setIs_experienced(helperProfileDTO.getCareExperience());
            profileUpdated = true;
        }

        // 자격증 정보가 있는 경우에만 업데이트
        if (helperProfileDTO.getCertificates() != null && !helperProfileDTO.getCertificates().isEmpty()) {
            List<TblHelperCert> certificates = helperCertRepository.findByTblHelperId(tblHelper.getId());

            for (HelperCertDTO helperCertDTO : helperProfileDTO.getCertificates()) {
                // 자격증 형식 검증
                validateCertificateFormat(helperCertDTO);
                
                // Q-net 인증 시도
                String qnetResult = checkCertificate(
                    tblHelper.getName(),
                    tblHelper.getBirthday(),
                    helperCertDTO.getCertNum(),
                    String.valueOf(helperCertDTO.getCertDateIssue()),
                    String.valueOf(helperCertDTO.getCertSerialNum())
                );

                certificateResults.put(helperCertDTO.getCertName(), qnetResult);

                // 기존 자격증 찾아서 업데이트
                TblHelperCert existingCert = certificates.stream()
                    .filter(cert -> helperCertDTO.getCertName().equals(cert.getCertName()))
                    .findFirst()
                    .orElse(null);

                if (existingCert != null) {
                    // 기존 자격증 업데이트
                    existingCert.setCertNum(helperCertDTO.getCertNum());
                    existingCert.setCertDateIssue(helperCertDTO.getCertDateIssue());
                    existingCert.setCertSerialNum(helperCertDTO.getCertSerialNum());
                } else {
                    // 새로운 자격증 추가
                    TblHelperCert newCert = TblHelperCert.builder()
                        .tblHelper(tblHelper)
                        .certName(helperCertDTO.getCertName())
                        .certNum(helperCertDTO.getCertNum())
                        .certDateIssue(helperCertDTO.getCertDateIssue())
                        .certSerialNum(helperCertDTO.getCertSerialNum())
                        .build();
                    certificates.add(newCert);
                }

                // Q-net 인증 실패 시 로그 기록 (fallback으로 DB에는 저장)
                if (!"VALID".equals(qnetResult)) {
                    log.warn("Q-net 자격증 인증 실패 - 자격증명: {}, 번호: {}, 결과: {}", 
                        helperCertDTO.getCertName(), helperCertDTO.getCertNum(), qnetResult);
                }
            }

            helperCertRepository.saveAll(certificates);
        }

        helperRepository.save(tblHelper);
        
        // 결과 구성
        result.put("message", "프로필이 정상적으로 업데이트 되었습니다.");
        result.put("profileUpdated", profileUpdated);
        result.put("certificatesUpdated", !certificateResults.isEmpty());
        if (!certificateResults.isEmpty()) {
            result.put("certificateVerificationResults", certificateResults);
        }
        
        return result;
    }

    @Override
    public WageUpdateResponse updateWage(UserDetails userDetails, WageUpdateDTO wageUpdateDTO) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));


        tblHelper.setWage(wageUpdateDTO.getWage());
        tblHelper.setWageState(wageUpdateDTO.getWageState());
        tblHelper.setWageNegotiation(wageUpdateDTO.getWageNegotiation());

        helperRepository.save(tblHelper);

        return WageUpdateResponse.builder()
                .email(tblUser.getEmail())
                .wage(wageUpdateDTO.getWage())
                .build();
    }

    @Override
    public HelperWorkLocationReponse workLocationSignUp(HelperWorkLocationDTO helperWorkLocationDTO, UserDetails userDetails) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        List<TblAddressFirst> addressFirstList = tblAddressFirstRepository.findAllById(helperWorkLocationDTO.getAddressFirstIds());
        List<TblAddressSecond> addressSecondList = tblAddressSecondRepository.findAllById(helperWorkLocationDTO.getAddressSecondIds());
        List<TblAddressThird> addressThirds = tblAddressThirdRepository.findAllById(helperWorkLocationDTO.getAddressThirdIds());

        if(addressFirstList.isEmpty() || addressSecondList.isEmpty() || addressThirds.isEmpty()) {
            throw new BadRequestException(INVALID_ADDR_INFO);
        }

        List<TblHelperWorkLocation> workLocations = addressFirstList.stream()
                .flatMap(first -> addressSecondList.stream()
                        .flatMap(second -> addressThirds.stream()
                                .map(third -> TblHelperWorkLocation.builder()
                                        .helper(tblHelper)
                                        .tblAddressFirst(first)
                                        .tblAddressSecond(second)
                                        .tblAddressThird(third)
                                        .build()))).toList();

        if(workLocations.isEmpty()) {
            throw new BadRequestException(NO_ADDRESS_INFO);
        }

        helperWorkLocationRepository.saveAll(workLocations);

        return HelperWorkLocationReponse.builder()
                .helperName(tblHelper.getName())
                .build();
    }

    @Override
    public HelperWorkTimeResponse workTimeSignUp(HelperWorkTimeRequestDTO helperWorkTimeRequestDTO, UserDetails userDetails) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        // 1. 중복되지 않는 새로운 데이터만 필터링
        List<TblHelperWorkTime> newWorkTimes = helperWorkTimeRequestDTO.getWorkTimes().stream()
                .filter(dto -> !helperWorkTimeRepository.existsByHelperAndDateAndStartTimeAndEndTime(
                        tblHelper, dto.getDay(), dto.getStartTime(), dto.getEndTime()))
                .map(dto -> TblHelperWorkTime.builder()
                        .helper(tblHelper)
                        .date(dto.getDay())
                        .startTime(dto.getStartTime())
                        .endTime(dto.getEndTime())
                        .negotiation(helperWorkTimeRequestDTO.getNegotiation())
                        .workTerm(helperWorkTimeRequestDTO.getWorkTerm())
                        .build())
                .collect(Collectors.toList());

        // 2. 저장할 데이터가 존재할 때만 저장
        if (!newWorkTimes.isEmpty()) {
            helperWorkTimeRepository.saveAll(newWorkTimes);
            return HelperWorkTimeResponse.builder()
                    .helperName(tblHelper.getName())
                    .build();
        }
        throw new BadRequestException(DUPLICATE_WORK_TIME);
    }

    @Override
    public HelperExperienceResponse experienceSignUp(HelperExperienceDTO helperExperienceDTO, UserDetails userDetails) {
        // 1. Helper 테이블 찾기, 없으면 예외 발생
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        tblHelper.setIs_experienced(true);
        helperRepository.save(tblHelper);

        // 1-2 Helper테이블이 1개 이상의 Experience 테이블을 가지고 있으면 중복 처리로 에러 발생
        int countExperience = helperExperienceRepository.countByHelperAndFieldAndHeStartDateAndHeEndDate(
                tblHelper,
                helperExperienceDTO.getField(),
                helperExperienceDTO.getHeStartDate(),
                helperExperienceDTO.getHeEndDate()
                );
        if(countExperience > 0) {
            throw new BadRequestException(HELPER_ALREADY_HAS_EXPERIENCE);
        }

        // 2. 엔티티 만들어서 회원 가입 진행, 가입 전 null값 체크
        if(HelperExperienceDTO.hasNullHelperExperienceRequestDTO(helperExperienceDTO)) {
            throw new BadRequestException(SIGNUP_HELPER_EXPERIENCE_INFO_NULL);
        }

        TblHelperExperience tblHelperExperience = TblHelperExperience.builder()
                .helper(tblHelper)
                .field(helperExperienceDTO.getField())
                .heStartDate(helperExperienceDTO.getHeStartDate())
                .heEndDate(helperExperienceDTO.getHeEndDate())
                .build();

        try {
            TblHelperExperience tblHelperExperienceSaved = helperExperienceRepository.save(tblHelperExperience);

            return HelperExperienceResponse.builder()
                    .helperName(tblHelperExperienceSaved.getHelper().getName())
                    .field(tblHelperExperienceSaved.getField())
                    .build();
        } catch (Exception e) {
            throw new BadRequestException(INTERNAL_SEVER_ERROR);
        }
    }

    @Override
    public HelperSearchResponse helperSearch(HelperSearchDTO helperSearchDTO) {
        List<TblHelper> helpers = helperRepository.findAll();

        List<HelperSearchResponse.HelperSearchInfo> helperSearchInfos = helpers.stream()
                .map(helper -> {
                    boolean[] checkInfo = new boolean[4];

                    int genderStr = 1;
                    // 성별 계산
                    if(!helperSearchDTO.getGenders().isEmpty() && helperSearchDTO.getGenders().contains(helper.getGender())) {
                        checkInfo[0] = true;
                        if(helper.getGender() == 0 && helperSearchDTO.getGenders().contains(0)) {
                            genderStr = 0;
                        }
                        else if(helper.getGender() == 1 && helperSearchDTO.getGenders().contains(1)) {
                            genderStr = 1;
                        }
                    }

                    // 나이, 계산
                    String ageGroup = "알 수 없음";
                    if (helper.getBirthday() != null && !helperSearchDTO.getAges().isEmpty()) {
                        try {
                            LocalDate birthDate = LocalDate.parse(helper.getBirthday());
                            int age = Period.between(birthDate, LocalDate.now()).getYears();
                            if (age >= 20 && age < 30) {
                                ageGroup = "20대";
                                checkInfo[1] = true;
                            }
                            else if (age >= 30 && age < 40) {
                                checkInfo[1] = true;
                                ageGroup = "30대";
                            }
                            else if (age >= 40 && age < 50) {
                                checkInfo[1] = true;
                                ageGroup = "40대";
                            }
                            else if (age >= 50) {
                                checkInfo[1] = true;
                                ageGroup = "50대 이상";
                            }
                        } catch (Exception e) {
                            throw new BadRequestException(AGE_CAL_EXCEPTION);
                        }
                    }

                    // 경력
                    String experience = "신입";
                    if (helper.getIs_experienced() != null && helper.getIs_experienced() && !helperSearchDTO.getExperiences().isEmpty()) {
                        List<TblHelperExperience> experiences = helperExperienceRepository.findByHelper(helper);

                        if (!experiences.isEmpty()) {
                            long totalYears = experiences.stream()
                                    .filter(exp -> exp.getHeEndDate() != null && exp.getHeStartDate() != null)
                                    .mapToLong(exp -> {
                                        long years = ChronoUnit.YEARS.between(
                                                exp.getHeStartDate().toLocalDate(),
                                                exp.getHeEndDate().toLocalDate()
                                        );
                                        return Math.max(0, years);
                                    })
                                    .sum();

                            if (totalYears == 1) {
                                experience = "1년";
                                checkInfo[2] = true;
                            } else if (totalYears >= 3 && totalYears <= 5) {
                                experience = "3~5년";
                                checkInfo[2] = true;
                            } else if (totalYears > 5) {
                                experience = "5년 이상";
                                checkInfo[2] = true;
                            }
                            else if(totalYears < 1) {
                                experience = "신입";
                                checkInfo[2] = true;
                            }
                        }
                    }

                    // 근무 가능 기간
                    String workTerm = "정보 없음";
                    List<TblHelperWorkTime> workTimes = helperWorkTimeRepository.findByHelper(helper);

                    if (!workTimes.isEmpty() && !helperSearchDTO.getTerms().isEmpty()) {
                        long size = workTimes.size();
                        long maxWorkTermValue = 0;
                        for(int i=0; i<size; i++) {
                            TblHelperWorkTime workTime = workTimes.get(i);
                            if(workTime.getWorkTerm() != null && !workTime.getWorkTerm().isEmpty()) {
                                long size2 = workTime.getWorkTerm().size();
                                for(int j=0; j<size2; j++) {
                                    maxWorkTermValue = Math.max(maxWorkTermValue, Integer.parseInt(String.valueOf(workTime.getWorkTerm().get(j))));
                                }
                            }
                        }

                        if (maxWorkTermValue <= 3) {
                            workTerm = "6개월";
                            checkInfo[3] = true;
                        } else if (maxWorkTermValue == 4) {
                            workTerm = "1년";
                            checkInfo[3] = true;
                        } else if (maxWorkTermValue == 5) {
                            workTerm = "2년";
                            checkInfo[3] = true;
                        } else {
                            workTerm = "2년 이상";
                            checkInfo[3] = true;
                        }
                    }

                    TblUser tblUser = memberRepository.findById(helper.getUser().getId())
                            .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));


                    if(checkInfo[0] || checkInfo[1] || checkInfo[2] || checkInfo[3]) {
                        return HelperSearchResponse.HelperSearchInfo.builder()
                                .email(tblUser.getEmail())
                                .name(helper.getName())
                                .gender(genderStr)
                                .age(ageGroup)
                                .experience(experience)
                                .workTerm(workTerm)
                                .introduce(helper.getIntroduce())
                                .build();
                    }
                    return null;
                })
                .collect(Collectors.toList());

            return HelperSearchResponse.builder()
                .helperSearchInfos(helperSearchInfos)
                .build();
    }

    @Override
    @Transactional
    public Map<String, String> saveCertificateByQNet(List<HelperCertDTO> helperCertDTO, UserDetails userDetails) {
        TblUser tblUser = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        List<TblHelperCert> certificates = helperCertRepository.findByTblHelperId(tblHelper.getId());
        Map<String, String> answers = new HashMap<>();
        
        for(HelperCertDTO helperCert : helperCertDTO) {
            // 자격증 형식 검증
            validateCertificateFormat(helperCert);
            
            // Q-net 인증 시도
            String qnetResult = checkCertificate(
                    tblHelper.getName(),
                    tblHelper.getBirthday(),
                    helperCert.getCertNum(),
                    String.valueOf(helperCert.getCertDateIssue()),
                    String.valueOf(helperCert.getCertSerialNum())
            );
            
            answers.put(helperCert.getCertName(), qnetResult);
            
            // Q-net 인증 결과와 상관없이 DB에 저장 (fallback 로직)
            // 기존 자격증 찾기
            TblHelperCert existingCert = certificates.stream()
                .filter(cert -> helperCert.getCertName().equals(cert.getCertName()))
                .findFirst()
                .orElse(null);

            if (existingCert != null) {
                // 기존 자격증 업데이트
                existingCert.setCertNum(helperCert.getCertNum());
                existingCert.setCertDateIssue(helperCert.getCertDateIssue());
                existingCert.setCertSerialNum(helperCert.getCertSerialNum());
            } else {
                // 새로운 자격증 추가
                TblHelperCert newCert = TblHelperCert.builder()
                    .tblHelper(tblHelper)
                    .certName(helperCert.getCertName())
                    .certNum(helperCert.getCertNum())
                    .certDateIssue(helperCert.getCertDateIssue())
                    .certSerialNum(helperCert.getCertSerialNum())
                    .build();
                certificates.add(newCert);
            }
            
            // Q-net 인증 실패 시 로그 기록
            if (!"VALID".equals(qnetResult)) {
                log.warn("Q-net 자격증 인증 실패하였으나 DB에 저장 - 자격증명: {}, 번호: {}, 결과: {}", 
                    helperCert.getCertName(), helperCert.getCertNum(), qnetResult);
            }
        }
        
        // 자격증 정보 DB에 저장 (Q-net 결과와 무관하게)
        helperCertRepository.saveAll(certificates);
        
        return answers;
    }


    @Override
    public String checkCertificate(String name, String birth, String certNo, String issueDate, String insideNo) {
        // 실제 결과
        String result = "UNKNOWN";

        // 쿠키 스토어 (세션 관리)
        CookieStore cookieStore = new BasicCookieStore();
        try (CloseableHttpClient client = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build()) {

            // 1) 사전 페이지 GET
            String preUrl = "https://www.q-net.or.kr/qlf006.do?id=qlf00601&gSite=Q&gId=";
            client.execute(new HttpGet(preUrl)).close();

            // 2) POST 요청
            String postUrl = "https://www.q-net.or.kr/qlf006.do?id=qlf00601s01";
            HttpPost post = new HttpPost(postUrl);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", "qlf00601s01"));
            params.add(new BasicNameValuePair("gSite", "Q"));
            params.add(new BasicNameValuePair("gId", ""));
            // 주민번호 앞 6자리, 혹은 생년월일
            params.add(new BasicNameValuePair("resdNo1", birth));
            // 성명
            params.add(new BasicNameValuePair("hgulNm", name));
            // 자격증번호
            params.add(new BasicNameValuePair("lcsNo", certNo));
            // 발급(등록) 연월일
            params.add(new BasicNameValuePair("qualExpDt", issueDate));
            // 자격증내지번호
            params.add(new BasicNameValuePair("lcsMngNo", insideNo));

            post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

            HttpResponse response = client.execute(post);
            String html = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            // 3) HTML 파싱
            Document doc = Jsoup.parse(html);

            // “해당결과가 없습니다” 판별
            if (html.contains("해당결과가 없습니다")) {
                result = "NOT_FOUND"; // or INVALID
            } else if (html.contains("유효")) {
                result = "VALID";
            } else {
                // 기타 케이스
                result = "UNKNOWN";
            }

        } catch (Exception e) {
            log.error("큐넷 스크래핑 중 오류 발생", e);
            result = "ERROR";
        }

        return result;
    }

    /**
     * 자격증 형식 검증 메서드
     * @param helperCertDTO 검증할 자격증 정보
     */
    private void validateCertificateFormat(HelperCertDTO helperCertDTO) {
        // 자격증 이름 검증
        if (helperCertDTO.getCertName() == null || helperCertDTO.getCertName().trim().isEmpty()) {
            throw new BadRequestException(ExceptionCode.INVALID_CERTIFICATE_NAME);
        }

        // 자격증 번호 검증 (요양보호사 자격증 형식: 숫자-숫자-숫자 형태)
        if (helperCertDTO.getCertNum() == null || helperCertDTO.getCertNum().trim().isEmpty()) {
            throw new BadRequestException(ExceptionCode.INVALID_CERTIFICATE_NUMBER);
        }

        // 요양보호사 자격증 번호 형식 검증 (예: 123456-1234-567890)
        String certNumPattern = "^\\d{6}-\\d{4}-\\d{6}$";
        if (!helperCertDTO.getCertNum().matches(certNumPattern)) {
            throw new BadRequestException(ExceptionCode.INVALID_CERTIFICATE_FORMAT);
        }

        // 발급일 검증
        if (helperCertDTO.getCertDateIssue() == null) {
            throw new BadRequestException(ExceptionCode.INVALID_CERTIFICATE_ISSUE_DATE);
        }

        // 발급일 형식 검증 (YYYYMMDD)
        String issueDateStr = String.valueOf(helperCertDTO.getCertDateIssue());
        if (issueDateStr.length() != 8 || !issueDateStr.matches("^\\d{8}$")) {
            throw new BadRequestException(ExceptionCode.INVALID_CERTIFICATE_ISSUE_DATE_FORMAT);
        }

        // 내지번호 검증
        if (helperCertDTO.getCertSerialNum() == null) {
            throw new BadRequestException(ExceptionCode.INVALID_CERTIFICATE_SERIAL_NUMBER);
        }

        // 내지번호는 1자리 이상의 숫자
        if (helperCertDTO.getCertSerialNum() <= 0) {
            throw new BadRequestException(ExceptionCode.INVALID_CERTIFICATE_SERIAL_NUMBER_FORMAT);
        }
    }

    @Override
    public HelperResponse getHelperDetail(HelperDetailDTO helperDetailDTO) {
        TblHelper helper = helperRepository.findById(helperDetailDTO.getHelperSeq())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));
        TblUser user = memberRepository.findById(helper.getUser().getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        List<TblHelperCert> certificates = helperCertRepository.findByTblHelperId(helper.getId());
        List<HelperCertDTO> certDTOList = new ArrayList<>();

        for(TblHelperCert tblHelperCert : certificates) {
            certDTOList.add(HelperCertDTO.builder()
                    .certName(tblHelperCert.getCertName())
                    .certNum(tblHelperCert.getCertNum())
                    .certDateIssue(tblHelperCert.getCertDateIssue())
                    .certSerialNum(tblHelperCert.getCertSerialNum())
                    .build());
        }

        return HelperResponse.builder()
                .id(helper.getId())
                .userEmail(user.getEmail())
                .name(helper.getName())
                .phone(helper.getPhone())
                .addressDetail(helper.getAddressDetail())
                .certificates(certDTOList)
                .carOwnYn(helper.isCarOwnYn())
                .eduYn(helper.isEduYn())
                .wage(helper.getWage())
                .wageState(helper.getWageState())
                .introduce(helper.getIntroduce())
                .careExperience(helper.getIs_experienced())
                .build();
    }


}