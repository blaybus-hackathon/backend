package com.balybus.galaxy.helper.serviceImpl.service;

import com.balybus.galaxy.address.domain.TblAddressSecond;
import com.balybus.galaxy.address.domain.TblAddressThird;
import com.balybus.galaxy.address.repository.TblAddressFirstRepository;
import com.balybus.galaxy.address.repository.TblAddressSecondRepository;
import com.balybus.galaxy.address.repository.TblAddressThirdRepository;
import com.balybus.galaxy.domain.tblCenter.TblCenter;
import com.balybus.galaxy.domain.tblCenter.TblCenterRepository;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.domain.tblMatching.MatchState;
import com.balybus.galaxy.domain.tblMatching.TblMatching;
import com.balybus.galaxy.domain.tblMatching.TblMatchingRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.helper.domain.*;
import com.balybus.galaxy.helper.dto.request.*;
import com.balybus.galaxy.helper.dto.response.*;
import com.balybus.galaxy.helper.repository.*;
import com.balybus.galaxy.helper.serviceImpl.HelperService;
import com.balybus.galaxy.login.dto.request.HelperCertDTO;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.member.repository.MemberRepository;
import com.balybus.galaxy.patient.domain.tblPatient.TblPatient;
import com.balybus.galaxy.patient.domain.tblPatient.TblPatientRepository;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLog;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLogRepository;
import com.balybus.galaxy.patient.domain.tblPatientTime.TblPatientTime;
import com.balybus.galaxy.patient.domain.tblPatientTime.TblPatientTimeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

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
import org.springframework.stereotype.Service;

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

    private final TblMatchingRepository tblMatchingRepository;
    private final TblPatientLogRepository patientLogRepository;
    private final TblPatientRepository tblPatientRepository;
    private final TblPatientTimeRepository tblPatientTimeRepository;
    private final TblCenterManagerRepository tblCenterManagerRepository;
    private final TblCenterRepository tblCenterRepository;

    @Override
    public HelperResponse getAllHelperInfo(UserDetails userDetails) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        List<TblHelperCert> certificates = helperCertRepository.findAllById(Collections.singleton(tblHelper.getId()));
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
                .build();
    }

    @Override
    public void updateProfile(UserDetails userDetails, HelperProfileDTO helperProfileDTO) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        tblHelper.setIntroduce(helperProfileDTO.getIntroduce());
        tblHelper.setIs_experienced(helperProfileDTO.getCareExperience());

        List<TblHelperCert> certificates = helperCertRepository.findAllById(tblHelper.getId());

        for(HelperCertDTO helperCertDTO : helperProfileDTO.getCertificates()) {
            for(TblHelperCert certificate : certificates) {
                if(helperCertDTO.getCertName().equals(certificate.getCertName())) {
                    certificate.setCertNum(helperCertDTO.getCertNum());
                    certificate.setCertDateIssue(helperCertDTO.getCertDateIssue());
                    certificate.setCertSerialNum(helperCertDTO.getCertSerialNum());
                    break;
                }
            }
        }

        helperCertRepository.saveAll(certificates);
        helperRepository.save(tblHelper);
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

        List<com.balybus.galaxy.address.domain.TblAddressFirst> addressFirstList = tblAddressFirstRepository.findAllById(helperWorkLocationDTO.getAddressFirstIds());
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
    public List<TblAddressFirstResponse> getFirstAddress() {
        return tblAddressFirstRepository.findAll().stream()
                .map(TblAddressFirstResponse::new)  // DTO로 변환
                .collect(Collectors.toList());
    }

    @Override
    public List<TblAddressThirdResponse> getThirdAddressBySecondId(Long asSeq) {
        List<TblAddressThird> entities = tblAddressThirdRepository.findByAddressSecond_Id(asSeq);

        return entities.stream()
                .map(TblAddressThirdResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TblAddressSecondResponse> getAddressSecondByFirstId(Long afSeq) {
        List<TblAddressSecond> entities = tblAddressSecondRepository.findByAddressFirst_Id(afSeq);

        return entities.stream()
                .map(TblAddressSecondResponse::new)
                .collect(Collectors.toList());
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
                    .filed(tblHelperExperienceSaved.getField())
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(DATA_VALIDATION_ERROR);

        } catch (TransactionSystemException e) {
            throw new BadRequestException(TRANSACTIONAL_ERROR);

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

                    String genderStr = "";
                    // 성별 계산
                    if(!helperSearchDTO.getGenders().isEmpty() && helperSearchDTO.getGenders().contains(helper.getGender())) {
                        checkInfo[0] = true;
                        if(helper.getGender() == 0 && helperSearchDTO.getGenders().contains(0)) {
                            genderStr = "남자";
                        }
                        else if(helper.getGender() == 1 && helperSearchDTO.getGenders().contains(1)) {
                            genderStr = "여자";
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
    public Map<String, String> saveCertificateByQNet(List<HelperCertDTO> helperCertDTO, UserDetails userDetails) {
        TblUser tblUser = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        Map<String, String> answers = new HashMap<>();
        for(HelperCertDTO helperCert : helperCertDTO) {
            answers.put(helperCert.getCertName(), checkCertificate(
                    tblHelper.getName(),
                    tblHelper.getBirthday(),
                    helperCert.getCertNum(),
                    String.valueOf(helperCert.getCertDateIssue()),
                    String.valueOf(helperCert.getCertSerialNum())
            ));
        }
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

    // 매칭 중인 노인 리스트 반환
    @Override
    public List<MatchingListResponseDTO> getHelperMatchingList(UserDetails userDetails) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        List<TblMatching> matchingList = tblMatchingRepository.findMatchingRequests(
                tblHelper.getId(), MatchState.MATCH_REQUEST
        );
        List<PatientNode> answerList = new ArrayList<>();
        for(TblMatching matching : matchingList) {
            Optional<TblPatientLog> patientLog = patientLogRepository.findById(matching.getPatientLog().getId());
            if (patientLog.isPresent()) {
                TblPatientLog tblPatientLog = patientLog.get();
                TblPatient tblPatient = tblPatientRepository.findById(tblPatientLog.getPatient().getId())
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_PATIENT));
                answerList.add(new PatientNode(tblPatientLog, tblPatient));
            }
        }

        List<MatchingListResponseDTO> matchingListResponseDTOList = new ArrayList<>();

        for(PatientNode patientNode : answerList) {
            TblPatientLog curPatientLog = patientNode.patientLog;
            TblPatient curPatient = patientNode.patient;

            List<Long> addressIdList = new ArrayList<>();
            addressIdList.add(curPatientLog.getTblAddressFirst().getId());
            addressIdList.add(curPatientLog.getTblAddressSecond().getId());
            addressIdList.add(curPatientLog.getTblAddressThird().getId());

            matchingListResponseDTOList.add(
                    MatchingListResponseDTO.builder()
                            .patientId(curPatient.getId())
                            .patientName(curPatient.getName())
                            .patientGender(curPatient.getGender())
                            .patientAge(curPatient.getBirthDate())
                            .workCategory(curPatient.getWorkType())
                            .patientAddressId(addressIdList)
                            .build()
            );
        }

        return matchingListResponseDTOList;
    }

    // 매칭 중인 리스트에서 자세히 보기 클릭시
    @Override
    public MatchedPatientResponseDTO getMatchedPatient(Long patientId, UserDetails userDetails) {

        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));


        // 주소
        TblPatient tblPatient = tblPatientRepository.findById(patientId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_PATIENT));
        List<String> patientAddrName = new ArrayList<>();
        patientAddrName.add(tblPatient.getTblAddressFirst().getName());
        patientAddrName.add(tblPatient.getTblAddressSecond().getName());
        patientAddrName.add(tblPatient.getTblAddressThird().getName());

        // 케어 필요 항목
        List<Integer> care_need_list = new ArrayList<>();
        if(tblPatient.getDementiaSymptom() == 1) {
            care_need_list.add(tblPatient.getDementiaSymptom());
        }
        if(tblPatient.getServiceMeal() == 1) {
            care_need_list.add(tblPatient.getServiceMeal());
        }
        if(tblPatient.getServiceToilet() == 1) {
            care_need_list.add(tblPatient.getServiceToilet());
        }
        if(tblPatient.getServiceMobility() == 1) {
            care_need_list.add(tblPatient.getServiceMobility());
        }
        if(tblPatient.getServiceDaily() == 1) {
            care_need_list.add(tblPatient.getServiceDaily());
        }

        // 요양 보호사 근무 가능 시간
        List<MatchedPatientResponseDTO.HelperWorkTime> helperWorkTimeList = new ArrayList<>();
        List<TblHelperWorkTime> tblHelperWorkTimeList = helperWorkTimeRepository.findAllById(Collections.singleton(tblHelper.getId()));
        for(TblHelperWorkTime tblHelperWorkTime : tblHelperWorkTimeList) {
            helperWorkTimeList.add(
                    MatchedPatientResponseDTO.HelperWorkTime.builder()
                            .day(tblHelperWorkTime.getDate())
                            .startTime(tblHelperWorkTime.getStartTime())
                            .endTime(tblHelperWorkTime.getEndTime())
                            .nego(tblHelperWorkTime.getNegotiation())
                            .workTerm(tblHelperWorkTime.getWorkTerm())
                            .build()
            );
        }

        // 어르신 근무 가능 시간
        List<MatchedPatientResponseDTO.PatientWorkTime> patientTimeList = new ArrayList<>();
        List<TblPatientTime> tblPatientTimeList = tblPatientTimeRepository.findAllById(Collections.singleton(tblPatient.getId()));
        for(TblPatientTime tblPatientTime : tblPatientTimeList) {
            patientTimeList.add(
                    MatchedPatientResponseDTO.PatientWorkTime.builder()
                            .day(tblPatientTime.getPtDate())
                            .startTime(tblPatientTime.getPtStartTime())
                            .endTime(tblPatientTime.getPtEndTime())
                            .build()
            );
        }

        return MatchedPatientResponseDTO.builder()
                .patientName(tblPatient.getName())
                .patientGender(tblPatient.getGender())
                .patientAge(tblPatient.getBirthDate())
                .workType(tblPatient.getWorkType())
                .addressList(patientAddrName)
                .helperWorkTimes(helperWorkTimeList)
                .patientWorkTimes(patientTimeList)
                .diseases(tblPatient.getDiseases())
                .careType(care_need_list)
                .wage(tblPatient.getWage())
                .build();
    }

    // 매칭 완료 리스트에서 어르신 정보 반환
    @Override
    public List<MatchedListResponseDTO> getMatchedPatientList(UserDetails userDetails) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        List<TblMatching> matchingList = tblMatchingRepository.findMatchingRequests(
                tblHelper.getId(), MatchState.MATCH_FIN
        );
        List<PatientNode> answerList = new ArrayList<>();

        for(TblMatching matching : matchingList) {
            Optional<TblPatientLog> patientLog = patientLogRepository.findById(matching.getPatientLog().getId());
            if (patientLog.isPresent()) {
                TblPatientLog tblPatientLog = patientLog.get();
                TblPatient tblPatient = tblPatientRepository.findById(tblPatientLog.getPatient().getId())
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_PATIENT));
                answerList.add(new PatientNode(tblPatientLog, tblPatient));
            }
        }



        List<MatchedListResponseDTO> matchingListResponseDTOList = new ArrayList<>();


        for(PatientNode patientNode : answerList) {
            TblPatientLog curPatientLog = patientNode.patientLog;
            TblPatient curPatient = patientNode.patient;
            TblCenterManager curCenterManager = tblCenterManagerRepository.findById(curPatient.getId())
                    .orElseThrow(() -> new BadRequestException(NOT_FOUND_MANAGER));
            TblCenter curCenter = tblCenterRepository.findById(curCenterManager.getId())
                    .orElseThrow(() -> new BadRequestException(NOT_FOUND_CENTER));

            // 주소
            List<Long> addressIdList = new ArrayList<>();
            addressIdList.add(curPatientLog.getTblAddressFirst().getId());
            addressIdList.add(curPatientLog.getTblAddressSecond().getId());
            addressIdList.add(curPatientLog.getTblAddressThird().getId());


            // 어르신 근무 가능 시간
            List<MatchedPatientResponseDTO.PatientWorkTime> patientWorkTimeList = new ArrayList<>();
            List<TblPatientTime> tblPatientTimeList = tblPatientTimeRepository.findAllById(Collections.singleton(curPatient.getId()));
            for(TblPatientTime tblPatientTime : tblPatientTimeList) {
                patientWorkTimeList.add(
                        MatchedPatientResponseDTO.PatientWorkTime.builder()
                                .day(tblPatientTime.getPtDate())
                                .startTime(tblPatientTime.getPtStartTime())
                                .endTime(tblPatientTime.getPtEndTime())
                                .build()
                );
            }

            // 케어 필요 항목
            List<Integer> care_need_list = new ArrayList<>();
            if(curPatient.getDementiaSymptom() == 1) {
                care_need_list.add(curPatient.getDementiaSymptom());
            }
            if(curPatient.getServiceMeal() == 1) {
                care_need_list.add(curPatient.getServiceMeal());
            }
            if(curPatient.getServiceToilet() == 1) {
                care_need_list.add(curPatient.getServiceToilet());
            }
            if(curPatient.getServiceMobility() == 1) {
                care_need_list.add(curPatient.getServiceMobility());
            }
            if(curPatient.getServiceDaily() == 1) {
                care_need_list.add(curPatient.getServiceDaily());
            }

            matchingListResponseDTOList.add(
                    MatchedListResponseDTO.builder()
                            .matchedPatientId(curPatient.getId())
                            .patientName(curPatient.getName())
                            .patientGender(curPatient.getGender())
                            .patientAge(curPatient.getBirthDate())
                            .managerPhoneNum(curCenter.getCenterTel())
                            .workType(curPatient.getWorkType())
                            .addressIdList(addressIdList)
                            .patientWorkDays(patientWorkTimeList)
                            .diseases(curPatient.getDiseases())
                            .careType(care_need_list)
                            .wage(curPatient.getWage())
                            .build()
            );
        }

        return matchingListResponseDTOList;
    }

    @Override
    public TblPatient getMathedPatientDetail(Long patientId) {
        return tblPatientRepository.findById(patientId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_PATIENT));
    }

    static class PatientNode {
        TblPatientLog patientLog;
        TblPatient patient;

        public PatientNode(TblPatientLog patientLog, TblPatient tblPatient) {
            this.patientLog = patientLog;
            this.patient = tblPatient;
        }
    }
}