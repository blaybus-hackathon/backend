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
import com.balybus.galaxy.careAssistant.dto.response.HelperCompleteProfileResponse.SectionResult;
import com.balybus.galaxy.careAssistant.repository.*;
import com.balybus.galaxy.careAssistant.dto.response.HelperWorkLocationDto;
import com.balybus.galaxy.careAssistant.dto.response.ImageDto;
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

        // 선호 지역
        List<TblHelperWorkLocation> workLocations = helperWorkLocationRepository.findByHelper_Id(tblHelper.getId());
        List<HelperWorkLocationDto> helperWorkLocationDtos = workLocations.stream()
                .map(wl -> HelperWorkLocationDto.builder()
                        .afSeq(wl.getTblAddressFirst().getId())
                        .asSeq(wl.getTblAddressSecond().getId())
                        .atSeq(wl.getTblAddressThird().getId())
                        .build())
                .toList();

        // 근무 가능 일정
        List<TblHelperWorkTime> workTimes = helperWorkTimeRepository.findByHelper(tblHelper);
        List<HelperWorkTimeDto> helperWorkTimeDtos = workTimes.stream()
                .map(wt -> HelperWorkTimeDto.builder()
                        .id(wt.getId())
                        .date(wt.getDate())
                        .startTime(wt.getStartTime())
                        .endTime(wt.getEndTime())
                        .negotiation(wt.getNegotiation())
                        .workTerm(wt.getWorkTerm())
                        .build())
                .toList();

        return HelperResponse.builder()
                .id(tblHelper.getId())
                .userEmail(tblUser.getEmail())
                .name(tblHelper.getName())
                .phone(tblHelper.getPhone())
                .addressDetail(tblHelper.getAddressDetail())
                .img(tblHelper.getImg() != null ? ImageDto.builder()
                        .id(tblHelper.getImg().getId())
                        .imgUuid(tblHelper.getImg().getImgUuid())
                        .imgOriginName(tblHelper.getImg().getImgOriginName())
                        .build() : null)
                .helperWorkLocation(helperWorkLocationDtos)
                .helperWorkTime(helperWorkTimeDtos)
                .careLevel(tblHelper.getCareLevel())
                .inmateState(tblHelper.getInmateState())
                .workType(tblHelper.getWorkType())
                .careGender(tblHelper.getCareGender())
                .serviceMeal(tblHelper.getServiceMeal())
                .serviceMobility(tblHelper.getServiceMobility())
                .serviceDaily(tblHelper.getServiceDaily())
                .certificates(certDTOList)
                .carOwnYn(tblHelper.isCarOwnYn())
                .eduYn(tblHelper.isEduYn())
                .wage(tblHelper.getWage())
                .wageState(tblHelper.getWageState())
                .introduce(tblHelper.getIntroduce())
                .careExperience(tblHelper.getIs_experienced())
                .wageNegotiation(tblHelper.getWageNegotiation())
                .build();
    }

    @Override
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

        // 자격증 정보가 있는 경우에만 업데이트 (Q-net 검증 없이)
        if (helperProfileDTO.getCertificates() != null && !helperProfileDTO.getCertificates().isEmpty()) {
            certificateResults = processCertificates(helperProfileDTO.getCertificates(), tblHelper, false);
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
                        .workTerm(helperWorkTimeRequestDTO.getWorkTerm().toString())
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
                        // workTerm이 String이므로 적절히 처리
                        for (TblHelperWorkTime workTime : workTimes) {
                            if (workTime.getWorkTerm() != null && !workTime.getWorkTerm().isEmpty()) {
                                workTerm = workTime.getWorkTerm();
                                checkInfo[3] = true;
                                break;
                            }
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
    public Map<String, String> saveCertificateByQNet(List<HelperCertDTO> helperCertDTO, UserDetails userDetails) {
        TblUser tblUser = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        // processCertificates 메서드를 사용하여 처리 (Q-net 검증 포함)
        return processCertificates(helperCertDTO, tblHelper, true);
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

    /**
     * 통합 프로필 업데이트 메서드
     * @param userDetails 현재 로그인한 사용자 정보
     * @param dto 통합 프로필 업데이트 DTO
     * @return 업데이트 결과
     */
    @Transactional
    public HelperCompleteProfileResponse updateCompleteProfile(UserDetails userDetails, HelperCompleteProfileDTO dto) {
        String username = userDetails.getUsername();
        
        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));
        
        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));
        
        HelperCompleteProfileResponse.HelperCompleteProfileResponseBuilder responseBuilder = 
            HelperCompleteProfileResponse.builder()
                .success(true)
                .message("프로필이 성공적으로 업데이트되었습니다.");
        
        // 1. 기본 프로필 정보 업데이트
        SectionResult profileResult = updateBasicProfile(tblHelper, dto);
        responseBuilder.profileResult(profileResult);
        
        // 2. 자격증 정보 업데이트
        if (dto.getCertificates() != null && !dto.getCertificates().isEmpty()) {
            boolean verifyQnet = dto.getVerifyQnet() != null ? dto.getVerifyQnet() : false;
            Map<String, String> certResults = processCertificates(dto.getCertificates(), tblHelper, verifyQnet);
            
            SectionResult certResult = SectionResult.builder()
                .updated(true)
                .success(true)
                .message("자격증 정보가 업데이트되었습니다.")
                .build();
            
            responseBuilder.certificateResult(certResult)
                .certificateVerificationResults(certResults);
        }
        
        // 3. 급여 정보 업데이트
        if (dto.getWage() != null || dto.getWageState() != null || dto.getWageNegotiation() != null) {
            SectionResult wageResult = updateWageInfo(tblHelper, dto);
            responseBuilder.wageResult(wageResult);
        }
        
        // 4. 근무 희망 지역 업데이트
        if (dto.getAddressFirstIds() != null && dto.getAddressSecondIds() != null && dto.getAddressThirdIds() != null) {
            SectionResult locationResult = updateWorkLocation(tblHelper, dto);
            responseBuilder.locationResult(locationResult);
        }
        
        // 5. 근무 가능 시간 업데이트
        if (dto.getWorkTimes() != null && !dto.getWorkTimes().isEmpty()) {
            SectionResult workTimeResult = updateWorkTime(tblHelper, dto);
            responseBuilder.workTimeResult(workTimeResult);
        }
        
        // 6. 경력 정보 업데이트
        if (dto.getExperience() != null) {
            SectionResult experienceResult = updateExperience(tblHelper, dto.getExperience());
            responseBuilder.experienceResult(experienceResult);
        }
        
        // 7. 돌봄 서비스 정보 업데이트
        SectionResult careServiceResult = updateCareService(tblHelper, dto);
        responseBuilder.careServiceResult(careServiceResult);
        
        // Helper 엔티티 저장
        helperRepository.save(tblHelper);
        
        // 업데이트된 전체 프로필 정보 조회
        HelperResponse updatedProfile = getAllHelperInfo(userDetails);
        responseBuilder.updatedProfile(updatedProfile);
        
        return responseBuilder.build();
    }
    
    private SectionResult updateBasicProfile(TblHelper tblHelper, HelperCompleteProfileDTO dto) {
        boolean updated = false;
        
        if (dto.getIntroduce() != null) {
            tblHelper.setIntroduce(dto.getIntroduce());
            updated = true;
        }
        
        if (dto.getCareExperience() != null) {
            tblHelper.setIs_experienced(dto.getCareExperience());
            updated = true;
        }
        
        if (dto.getCarOwnYn() != null) {
            tblHelper.setCarOwnYn(dto.getCarOwnYn());
            updated = true;
        }
        
        if (dto.getEduYn() != null) {
            tblHelper.setEduYn(dto.getEduYn());
            updated = true;
        }
        
        return SectionResult.builder()
            .updated(updated)
            .success(true)
            .message(updated ? "기본 프로필이 업데이트되었습니다." : "업데이트할 프로필 정보가 없습니다.")
            .build();
    }
    
    private SectionResult updateWageInfo(TblHelper tblHelper, HelperCompleteProfileDTO dto) {
        boolean updated = false;
        
        if (dto.getWage() != null) {
            tblHelper.setWage(dto.getWage());
            updated = true;
        }
        
        if (dto.getWageState() != null) {
            tblHelper.setWageState(dto.getWageState());
            updated = true;
        }
        
        if (dto.getWageNegotiation() != null) {
            tblHelper.setWageNegotiation(dto.getWageNegotiation());
            updated = true;
        }
        
        return SectionResult.builder()
            .updated(updated)
            .success(true)
            .message(updated ? "급여 정보가 업데이트되었습니다." : "업데이트할 급여 정보가 없습니다.")
            .build();
    }
    
    private SectionResult updateWorkLocation(TblHelper tblHelper, HelperCompleteProfileDTO dto) {
        try {
            // 기존 근무 희망 지역 삭제
            List<TblHelperWorkLocation> existingLocations = helperWorkLocationRepository.findByHelper_Id(tblHelper.getId());
            helperWorkLocationRepository.deleteAll(existingLocations);
            
            // 새로운 근무 희망 지역 추가
            List<TblAddressFirst> addressFirstList = tblAddressFirstRepository.findAllById(dto.getAddressFirstIds());
            List<TblAddressSecond> addressSecondList = tblAddressSecondRepository.findAllById(dto.getAddressSecondIds());
            List<TblAddressThird> addressThirdList = tblAddressThirdRepository.findAllById(dto.getAddressThirdIds());
            
            if (addressFirstList.isEmpty() || addressSecondList.isEmpty() || addressThirdList.isEmpty()) {
                return SectionResult.builder()
                    .updated(false)
                    .success(false)
                    .error("유효하지 않은 주소 정보입니다.")
                    .build();
            }
            
            List<TblHelperWorkLocation> workLocations = new ArrayList<>();
            for (int i = 0; i < Math.min(addressFirstList.size(), Math.min(addressSecondList.size(), addressThirdList.size())); i++) {
                TblHelperWorkLocation location = TblHelperWorkLocation.builder()
                    .helper(tblHelper)
                    .tblAddressFirst(addressFirstList.get(i))
                    .tblAddressSecond(addressSecondList.get(i))
                    .tblAddressThird(addressThirdList.get(i))
                    .build();
                workLocations.add(location);
            }
            
            helperWorkLocationRepository.saveAll(workLocations);
            
            return SectionResult.builder()
                .updated(true)
                .success(true)
                .message("근무 희망 지역이 업데이트되었습니다.")
                .build();
        } catch (Exception e) {
            return SectionResult.builder()
                .updated(false)
                .success(false)
                .error("근무 희망 지역 업데이트 중 오류가 발생했습니다: " + e.getMessage())
                .build();
        }
    }
    
    private SectionResult updateWorkTime(TblHelper tblHelper, HelperCompleteProfileDTO dto) {
        try {
            List<TblHelperWorkTime> newWorkTimes = dto.getWorkTimes().stream()
                .filter(wt -> !helperWorkTimeRepository.existsByHelperAndDateAndStartTimeAndEndTime(
                    tblHelper, wt.getDay(), wt.getStartTime(), wt.getEndTime()))
                .map(wt -> TblHelperWorkTime.builder()
                    .helper(tblHelper)
                    .date(wt.getDay())
                    .startTime(wt.getStartTime())
                    .endTime(wt.getEndTime())
                    .negotiation(dto.getNegotiation())
                    .workTerm(dto.getWorkTerm() != null ? dto.getWorkTerm().toString() : null)
                    .build())
                .collect(Collectors.toList());
            
            if (!newWorkTimes.isEmpty()) {
                helperWorkTimeRepository.saveAll(newWorkTimes);
                return SectionResult.builder()
                    .updated(true)
                    .success(true)
                    .message("근무 가능 시간이 업데이트되었습니다.")
                    .build();
            }
            
            return SectionResult.builder()
                .updated(false)
                .success(true)
                .message("추가할 새로운 근무 시간이 없습니다.")
                .build();
        } catch (Exception e) {
            return SectionResult.builder()
                .updated(false)
                .success(false)
                .error("근무 가능 시간 업데이트 중 오류가 발생했습니다: " + e.getMessage())
                .build();
        }
    }
    
    private SectionResult updateExperience(TblHelper tblHelper, HelperExperienceDTO experienceDTO) {
        try {
            if (HelperExperienceDTO.hasNullHelperExperienceRequestDTO(experienceDTO)) {
                return SectionResult.builder()
                    .updated(false)
                    .success(false)
                    .error("경력 정보가 불완전합니다.")
                    .build();
            }
            
            int countExperience = helperExperienceRepository.countByHelperAndFieldAndHeStartDateAndHeEndDate(
                tblHelper,
                experienceDTO.getField(),
                experienceDTO.getHeStartDate(),
                experienceDTO.getHeEndDate()
            );
            
            if (countExperience > 0) {
                return SectionResult.builder()
                    .updated(false)
                    .success(false)
                    .error("이미 등록된 경력입니다.")
                    .build();
            }
            
            TblHelperExperience experience = TblHelperExperience.builder()
                .helper(tblHelper)
                .field(experienceDTO.getField())
                .heStartDate(experienceDTO.getHeStartDate())
                .heEndDate(experienceDTO.getHeEndDate())
                .build();
            
            helperExperienceRepository.save(experience);
            tblHelper.setIs_experienced(true);
            
            return SectionResult.builder()
                .updated(true)
                .success(true)
                .message("경력 정보가 업데이트되었습니다.")
                .build();
        } catch (Exception e) {
            return SectionResult.builder()
                .updated(false)
                .success(false)
                .error("경력 정보 업데이트 중 오류가 발생했습니다: " + e.getMessage())
                .build();
        }
    }
    
    private SectionResult updateCareService(TblHelper tblHelper, HelperCompleteProfileDTO dto) {
        boolean updated = false;
        
        if (dto.getCareLevel() != null) {
            tblHelper.setCareLevel(dto.getCareLevel());
            updated = true;
        }
        
        if (dto.getInmateState() != null) {
            tblHelper.setInmateState(dto.getInmateState());
            updated = true;
        }
        
        if (dto.getWorkType() != null) {
            tblHelper.setWorkType(dto.getWorkType());
            updated = true;
        }
        
        if (dto.getCareGender() != null) {
            tblHelper.setCareGender(dto.getCareGender());
            updated = true;
        }
        
        if (dto.getServiceMeal() != null) {
            tblHelper.setServiceMeal(dto.getServiceMeal());
            updated = true;
        }
        
        if (dto.getServiceMobility() != null) {
            tblHelper.setServiceMobility(dto.getServiceMobility());
            updated = true;
        }
        
        if (dto.getServiceDaily() != null) {
            tblHelper.setServiceDaily(dto.getServiceDaily());
            updated = true;
        }
        
        return SectionResult.builder()
            .updated(updated)
            .success(true)
            .message(updated ? "돌봄 서비스 정보가 업데이트되었습니다." : "업데이트할 돌봄 서비스 정보가 없습니다.")
            .build();
    }

    /**
     * 자격증 처리를 위한 private 메서드
     * @param certificates 처리할 자격증 리스트
     * @param tblHelper 요양보호사 엔티티
     * @param verifyQnet Q-net 검증 여부
     * @return 처리 결과 Map
     */
    private Map<String, String> processCertificates(List<HelperCertDTO> certificates, TblHelper tblHelper, boolean verifyQnet) {
        List<TblHelperCert> existingCerts = helperCertRepository.findByTblHelperId(tblHelper.getId());
        Map<String, String> results = new HashMap<>();
        
        for (HelperCertDTO certDTO : certificates) {
            try {
                // 자격증 형식 검증
                validateCertificateFormat(certDTO);
                
                String verificationResult = "NOT_VERIFIED";
                
                // Q-net 검증이 요청된 경우에만 수행
                if (verifyQnet) {
                    verificationResult = checkCertificate(
                        tblHelper.getName(),
                        tblHelper.getBirthday(),
                        certDTO.getCertNum(),
                        String.valueOf(certDTO.getCertDateIssue()),
                        String.valueOf(certDTO.getCertSerialNum())
                    );
                }
                
                results.put(certDTO.getCertName(), verificationResult);
                
                // 기존 자격증 찾기
                TblHelperCert existingCert = existingCerts.stream()
                    .filter(cert -> certDTO.getCertName().equals(cert.getCertName()))
                    .findFirst()
                    .orElse(null);
                
                if (existingCert != null) {
                    // 기존 자격증 업데이트
                    existingCert.setCertNum(certDTO.getCertNum());
                    existingCert.setCertDateIssue(certDTO.getCertDateIssue());
                    existingCert.setCertSerialNum(certDTO.getCertSerialNum());
                } else {
                    // 새로운 자격증 추가
                    TblHelperCert newCert = TblHelperCert.builder()
                        .tblHelper(tblHelper)
                        .certName(certDTO.getCertName())
                        .certNum(certDTO.getCertNum())
                        .certDateIssue(certDTO.getCertDateIssue())
                        .certSerialNum(certDTO.getCertSerialNum())
                        .build();
                    existingCerts.add(newCert);
                }
                
                if (verifyQnet && !"VALID".equals(verificationResult)) {
                    log.warn("Q-net 자격증 인증 실패 - 자격증명: {}, 번호: {}, 결과: {}", 
                        certDTO.getCertName(), certDTO.getCertNum(), verificationResult);
                }
            } catch (BadRequestException e) {
                results.put(certDTO.getCertName(), "VALIDATION_FAILED: " + e.getMessage());
                log.error("자격증 검증 실패 - 자격증명: {}, 오류: {}", certDTO.getCertName(), e.getMessage());
            }
        }
        
        // 자격증 정보 DB에 저장
        helperCertRepository.saveAll(existingCerts);
        
        return results;
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

        // 선호 지역
        List<TblHelperWorkLocation> workLocations = helperWorkLocationRepository.findByHelper_Id(helper.getId());
        List<HelperWorkLocationDto> helperWorkLocationDtos = workLocations.stream()
                .map(wl -> HelperWorkLocationDto.builder()
                        .afSeq(wl.getTblAddressFirst().getId())
                        .asSeq(wl.getTblAddressSecond().getId())
                        .atSeq(wl.getTblAddressThird().getId())
                        .build())
                .toList();

        // 근무 가능 일정
        List<TblHelperWorkTime> workTimes = helperWorkTimeRepository.findByHelper(helper);
        List<HelperWorkTimeDto> helperWorkTimeDtos = workTimes.stream()
                .map(wt -> HelperWorkTimeDto.builder()
                        .id(wt.getId())
                        .date(wt.getDate())
                        .startTime(wt.getStartTime())
                        .endTime(wt.getEndTime())
                        .negotiation(wt.getNegotiation())
                        .workTerm(wt.getWorkTerm())
                        .build())
                .toList();

        return HelperResponse.builder()
                .id(helper.getId())
                .userEmail(user.getEmail())
                .name(helper.getName())
                .phone(helper.getPhone())
                .addressDetail(helper.getAddressDetail())
                .img(helper.getImg() != null ? ImageDto.builder()
                        .id(helper.getImg().getId())
                        .imgUuid(helper.getImg().getImgUuid())
                        .imgOriginName(helper.getImg().getImgOriginName())
                        .build() : null)
                .helperWorkLocation(helperWorkLocationDtos)
                .helperWorkTime(helperWorkTimeDtos)
                .careLevel(helper.getCareLevel())
                .inmateState(helper.getInmateState())
                .workType(helper.getWorkType())
                .careGender(helper.getCareGender())
                .serviceMeal(helper.getServiceMeal())
                .serviceMobility(helper.getServiceMobility())
                .serviceDaily(helper.getServiceDaily())
                .certificates(certDTOList)
                .carOwnYn(helper.isCarOwnYn())
                .eduYn(helper.isEduYn())
                .wage(helper.getWage())
                .wageState(helper.getWageState())
                .introduce(helper.getIntroduce())
                .careExperience(helper.getIs_experienced())
                .wageNegotiation(helper.getWageNegotiation())
                .build();
    }
}