package com.balybus.galaxy.patient.service.serviceImpl;

import com.balybus.galaxy.address.domain.TblAddressFirst;
import com.balybus.galaxy.address.domain.TblAddressSecond;
import com.balybus.galaxy.address.domain.TblAddressThird;
import com.balybus.galaxy.address.serviceImpl.service.TblAddressFirstServiceImpl;
import com.balybus.galaxy.address.serviceImpl.service.TblAddressSecondServiceImpl;
import com.balybus.galaxy.address.serviceImpl.service.TblAddressThirdServiceImpl;
import com.balybus.galaxy.domain.tblCare.TblCareRepository;
import com.balybus.galaxy.domain.tblCare.TblCareTopEnum;
import com.balybus.galaxy.domain.tblCare.service.TblCareServiceImpl;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.domain.tblMatching.MatchingServiceImpl;
import com.balybus.galaxy.global.common.CommonServiceImpl;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.file.service.FileService;
import com.balybus.galaxy.login.serviceImpl.loginAuth.LoginAuthCheckServiceImpl;
import com.balybus.galaxy.patient.domain.tblPatient.TblPatient;
import com.balybus.galaxy.patient.domain.tblPatient.TblPatientRepository;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLog;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLogRepository;
import com.balybus.galaxy.patient.domain.tblPatientTime.TblPatientTime;
import com.balybus.galaxy.patient.domain.tblPatientTime.TblPatientTimeRepository;
import com.balybus.galaxy.patient.domain.tblPatientTimeLog.TblPatientTimeLog;
import com.balybus.galaxy.patient.domain.tblPatientTimeLog.TblPatientTimeLogRepository;
import com.balybus.galaxy.patient.dto.baseDto.PatientBaseDto;
import com.balybus.galaxy.patient.dto.PatientRequestDto;
import com.balybus.galaxy.patient.dto.PatientResponseDto;
import com.balybus.galaxy.patient.service.PatientService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientServiceImpl implements PatientService {

    private final TblPatientRepository patientRepository;
    private final TblPatientTimeRepository patientTimeRepository;
    private final TblPatientLogRepository patientLogRepository;
    private final TblPatientTimeLogRepository patientTimeLogRepository;
    private final TblCareRepository careRepository;

    private final LoginAuthCheckServiceImpl loginAuthCheckService;
    private final MatchingServiceImpl matchingService;
    private final TblCareServiceImpl careService;
    private final CommonServiceImpl commonService;
    private final TblAddressFirstServiceImpl firstAddressService;
    private final TblAddressSecondServiceImpl secondAddressService;
    private final TblAddressThirdServiceImpl thirdAddressService;
    private final FileService fileService;

    /**
     * 관리자 사용자의 어르신 정보 접근 권한 확인
     * @param patientSeq Long: 어르신(TblPatient) 구분자
     * @param managerSeq Long: 센터 관리자(TblCenterManager) 구분자
     * @return TblPatient
     */
    private TblPatient checkPatientManagerAuth(Long patientSeq, Long managerSeq){
        Optional<TblPatient> patientOpt = patientRepository.findById(patientSeq);
        if(patientOpt.isEmpty()) throw new BadRequestException(ExceptionCode.NOT_FOUND_PATIENT);
        TblPatient patient = patientOpt.get();
        if(!patient.getManager().getId().equals(managerSeq))
            throw new BadRequestException(ExceptionCode.UNAUTHORIZED_UPDATE);

        return patient;
    }


    /**
     * 어르신 정보 등록
     * @param userEmail String:토큰 조회 결과 사용자 이메일 데이터
     * @param dto PatientRequestDto.SavePatientInfo
     * @return PatientResponseDto.SavePatientInfo
     */
    @Override
    @Transactional
    public PatientResponseDto.SavePatientInfo savePatientInfo(String userEmail, PatientRequestDto.SavePatientInfo dto) {
        //1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        //2. 어르신 정보 등록
        //2-1. 주소 정보 검증
        TblAddressFirst firstAddress = firstAddressService.validationCheck(dto.getAfSeq());
        TblAddressSecond secondAddress = secondAddressService.validationCheck(dto.getAfSeq(), dto.getAsSeq());
        TblAddressThird thirdAddress = thirdAddressService.validationCheck(dto.getAsSeq(), dto.getAtSeq());

        //2-2. 어르신 정보 entity 전환 및 저장
        TblPatient patient = patientRepository.save(dto.toEntity(centerManager, firstAddress, secondAddress, thirdAddress));

        //3. 어르신 돌봄 시간 요일(리스트 정보) entity 전환 및 저장
        List<TblPatientTime> savePatientTimeList = new ArrayList<>();
        for(PatientBaseDto.SavePatientTimeInfo ptDto : dto.getTimeList())
            savePatientTimeList.add(ptDto.toEntity(patient));

        patientTimeRepository.saveAll(savePatientTimeList);

        //4. 어르신 정보 구분자 값, 담당자 이메일 반환
        return PatientResponseDto.SavePatientInfo.builder()
                .patientSeq(patient.getId())
                .managerEmail(userEmail)
                .build();
    }

    /**
     * 어르신 정보 수정
     * @param userEmail String:토큰 조회 결과 사용자 이메일 데이터
     * @param dto PatientRequestDto.UpdatePatientInfo
     * @return PatientResponseDto.UpdatePatientInfo
     */
    @Override
    @Transactional
    public PatientResponseDto.UpdatePatientInfo updatePatientInfo(String userEmail, PatientRequestDto.UpdatePatientInfo dto) {
        //1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        //2. 어르신 정보 조회 (어르신 구분자 & 관리자 구분자) 및 수정
        //2-1. 어르신 데이터 조회
        TblPatient patient = checkPatientManagerAuth(dto.getPatientSeq(), centerManager.getId());

        //2-2. 주소 정보 검증
        TblAddressFirst firstAddress = firstAddressService.validationCheck(dto.getAfSeq());
        TblAddressSecond secondAddress = secondAddressService.validationCheck(dto.getAfSeq(), dto.getAsSeq());
        TblAddressThird thirdAddress = thirdAddressService.validationCheck(dto.getAsSeq(), dto.getAtSeq());

        //2-3. 데이터 수정
        patient.basicUpdate(dto, firstAddress, secondAddress, thirdAddress);

        //3. 어르신 돌봄 시간 요일 조회 및 삭제
        List<TblPatientTime> patientTimeList = patientTimeRepository.findByPatient_Id(patient.getId());
        patientTimeRepository.deleteAll(patientTimeList);

        //4. 어르신 돌봄 시간 요일(리스트 정보) entity 전환 및 저장
        List<TblPatientTime> savePatientTimeList = new ArrayList<>();
        for(PatientBaseDto.SavePatientTimeInfo ptDto : dto.getTimeList())
            savePatientTimeList.add(ptDto.toEntity(patient));

        patientTimeRepository.saveAll(savePatientTimeList);

        //5. 어르신 정보 구분자 값, 이름, 생년월일 중 연도 반환
        return PatientResponseDto.UpdatePatientInfo.builder()
                .patientSeq(patient.getId())
                .managerEmail(userEmail)
                .build();
    }

    /**
     * 어르신 정보 상세 조회
     * @param userEmail String:토큰 조회 결과 사용자 이메일 데이터
     * @param patientSeq Long
     * @return PatientResponseDto.GetOnePatientInfo
     */
    @Override
    public PatientResponseDto.GetOnePatientInfo getOnePatientInfo(String userEmail, Long patientSeq) {
        //1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        //2. 어르신 정보 조회 (어르신 구분자 & 관리자 구분자)
        TblPatient patient = checkPatientManagerAuth(patientSeq, centerManager.getId());

        //3. 어르신 돌봄 시간 요일 조회
        List<TblPatientTime> patientTimeList = patientTimeRepository.findByPatient_Id(patient.getId());

        //4. 반환 dto 생성
        PatientResponseDto.GetOnePatientInfo resultDto = new PatientResponseDto.GetOnePatientInfo(patient, patientTimeList);
        resultDto.setCareChoice(careService.getCareChoiceList(resultDto, false));
        resultDto.setCareBaseDtoNull();
        return resultDto;
    }

    @Override
    public PatientResponseDto.GetPatientList getPatientList(String userEmail, PatientRequestDto.GetPatientList dto) {
        //1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        //2. 해당 관리자가 관리중인 어르신 리스트 조회
        Pageable page = PageRequest.of(
                dto.getPageNo()==null ? 0 : dto.getPageNo()
                , dto.getPageSize()==null ? 10 : dto.getPageSize()
                , Sort.by(Sort.Order.asc("name"), Sort.Order.desc("birthDate"), Sort.Order.desc("id")));
        Page<TblPatient> listPage = patientRepository.findByManagerId(centerManager.getId(), page);
        List<TblPatient> patientEntityList = listPage.getContent();

        //3. 성별/근무종류/주소지/장기요양등급 각 항목 이름 조회 및 dto 리스트 정리
        List<PatientResponseDto.GetPatientListInfo> resultList = new ArrayList<>();
        for (TblPatient entity : patientEntityList) {
            resultList.add(
                    PatientResponseDto.GetPatientListInfo.builder()
                            .patientSeq(entity.getId())
                            .imgAddress(entity.getImg() == null ? null : fileService.getOneImgUrl(entity.getImg().getId()))
                            .name(entity.getName())
                            .age(commonService.calculateAge(LocalDate.parse(entity.getBirthDate(), java.time.format.DateTimeFormatter.BASIC_ISO_DATE)))
                            .address(commonService.fullAddressString(entity.getTblAddressFirst(), entity.getTblAddressSecond(), entity.getTblAddressThird()))
                            .genderStr(careRepository.findCalNameListStr(TblCareTopEnum.GENDER.getCareSeq(), entity.getGender()))
                            .careLevelStr(careRepository.findCalNameListStr(TblCareTopEnum.CARE_LEVEL.getCareSeq(), entity.getCareLevel()))
                            .workType(careRepository.findCalNameListStr(TblCareTopEnum.WORK_TYPE.getCareSeq(), entity.getInmateState()))
                            .build());
        }

        //4. 결과 반환
        return PatientResponseDto.GetPatientList.builder()
                .totalPage(listPage.getTotalPages())
                .totalEle(listPage.getTotalElements())
                .hasNext(listPage.hasNext())
                .list(resultList)
                .build();
    }

    /**
     * 어르신 공고 등록
     * @param userDetails UserDetails:토큰 조회 결과 데이터
     * @param dto PatientRequestDto.RecruitHelper
     * @return PatientResponseDto.RecruitHelper
     */
    @Override
    @Transactional
    public PatientResponseDto.RecruitHelper recruitHelper(UserDetails userDetails, PatientRequestDto.RecruitHelper dto) {
        //1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userDetails.getUsername());

        //2. 어르신 정보 조회 (어르신 구분자 & 관리자 구분자) 및 수정 & 로그 등록
        //2-1. 어르신 데이터 조회
        TblPatient patient = checkPatientManagerAuth(dto.getPatientSeq(), centerManager.getId());

        //2-2. 주소 정보 검증
        TblAddressFirst firstAddress = firstAddressService.validationCheck(dto.getAfSeq());
        TblAddressSecond secondAddress = secondAddressService.validationCheck(dto.getAfSeq(), dto.getAsSeq());
        TblAddressThird thirdAddress = thirdAddressService.validationCheck(dto.getAsSeq(), dto.getAtSeq());

        //2-3. TblPatientTime 업데이트 여부에 설정에 따른 어르신 데이터 수정
        if(dto.getLinkingYn()){
            //2-3-1. 어르신 데이터 수정
            patient.basicUpdate(dto, firstAddress, secondAddress, thirdAddress);
            //2-3-2. 어르신 돌봄 시간 요일 조회 및 삭제
            List<TblPatientTime> patientTimeList = patientTimeRepository.findByPatient_Id(patient.getId());
            patientTimeRepository.deleteAll(patientTimeList);
            //2-3-3. 어르신 돌봄 시간 요일(리스트 정보) entity 전환 및 저장
            List<TblPatientTime> savePatientTimeList = new ArrayList<>();
            for(PatientBaseDto.SavePatientTimeInfo ptDto : dto.getTimeList()){
                savePatientTimeList.add(ptDto.toEntity(patient));
            }
            patientTimeRepository.saveAll(savePatientTimeList);
        }

        //2-4. 임금 계산
        Map<String, Double> calWage = calWage(dto.getWageState(), dto.getWage(), dto.getTimeList());

        //2-5. 어르신 로그 entity 전환 및 저장
        TblPatientLog patientLog = patientLogRepository.save(
                dto.toEntity(
                        patient, centerManager,
                        calWage.get("timeWage"), calWage.get("dayWage"), calWage.get("weekWage"),
                        firstAddress, secondAddress, thirdAddress));

        //3. TblPatientTimeLog 어르신 돌봄 시간 요일(리스트 정보) entity 전환 및 저장
        List<TblPatientTimeLog> savePatientTimeLogList = new ArrayList<>();
        for(PatientBaseDto.SavePatientTimeInfo ptDto : dto.getTimeList()){
            savePatientTimeLogList.add(ptDto.toLogEntity(patientLog));
        }

        patientTimeLogRepository.saveAll(savePatientTimeLogList);

        //4. 요양보호사 추천 리스트 매칭
        matchingService.matchingSystem(patientLog.getId());

        return PatientResponseDto.RecruitHelper.builder()
                .plSeq(patientLog.getId())
                .patientSeq(patient.getId())
                .name(patient.getName())
                .birthYear(patient.getBirthDate().substring(0, 4))
                .build();
    }

    private Map<String, Double> calWage(int wageState, int wage, List<PatientBaseDto.SavePatientTimeInfo> timeList){
        Map<String, Double> calWage = new HashMap<>();
        double timeWage = 0;    // 시급
        double dayWage = 0;     // 일급
        double weekWage = 0;    // 주급
        double dayCnt = timeList.size();
        double timeCnt = 0;

        // 구인하는 돌봄 전체 시간
        for(PatientBaseDto.SavePatientTimeInfo ptDto : timeList){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime startTime = LocalTime.parse(ptDto.getPtStartTime(), formatter);
            LocalTime endTime = LocalTime.parse(ptDto.getPtEndTime(), formatter);
            Duration duration = Duration.between(startTime, endTime);

            // 시간을 소수 형태로 변환 (분을 시간으로 변환)
            timeCnt += (duration.toMinutes() / 60.0);
        }

        if(wageState == 1) {
            // patient => 시급 timeWage
            // 시급 = 시급 | 주급 = 선택된 요일별 전체 시간 * 시급 | 일급	= 주급 / 선택된 요일 수
            timeWage = wage;
            weekWage = wage * timeCnt;
            dayWage = weekWage / dayCnt;
        } else if (wageState == 2) {
            // patient => 일급 dayWage
            // 일급 = 일급 | 주급 = 일급 * 선택된 요일 수 | 시급 = 주급 / 선택된 요일별 전체 시간
            dayWage = wage;
            weekWage = wage * dayCnt;
            timeWage = weekWage / timeCnt;
        } else if (wageState == 3) {
            // patient => 주급 weekWage
            // 주급 = 주급 | 일급 = 주급 / 선택된 요일 수 | 시급 = 주급 / 선택된 요일별 전체 시간
            weekWage = wage;
            timeWage = wage / timeCnt;
            dayWage = wage / dayCnt;
        }

        calWage.put("timeWage", timeWage);
        calWage.put("dayWage", dayWage);
        calWage.put("weekWage", weekWage);
        return calWage;
    }


    @Override
    public PatientResponseDto.GetRecruitList getRecruitList(String userEmail, PatientRequestDto.GetRecruitList dto) {
        //1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        //2. 해당 관리자가 관리중인 어르신 공고 리스트 조회
        Pageable page = PageRequest.of(
                dto.getPageNo()==null ? 0 : dto.getPageNo()
                , dto.getPageSize()==null ? 10 : dto.getPageSize()
                , Sort.by(Sort.Order.asc("name"), Sort.Order.desc("birthDate"), Sort.Order.desc("id")));
        Page<TblPatientLog> listPage = patientLogRepository.findByManagerId(centerManager.getId(), page);
        List<TblPatientLog> patientEntityList = listPage.getContent();

        //3. 성별/근무종류/주소지/장기요양등급 각 항목 이름 조회 및 dto 리스트 정리
        List<PatientResponseDto.GetRecruitListInfo> resultList = new ArrayList<>();
        for (TblPatientLog entity : patientEntityList) {
            resultList.add(
                    PatientResponseDto.GetRecruitListInfo.builder()
                            .patientLogSeq(entity.getId())
                            .imgAddress(entity.getPatient().getImg() == null ? null : fileService.getOneImgUrl(entity.getPatient().getImg().getId()))
                            .name(entity.getName())
                            .age(commonService.calculateAge(LocalDate.parse(entity.getBirthDate(), java.time.format.DateTimeFormatter.BASIC_ISO_DATE)))
                            .address(commonService.fullAddressString(entity.getTblAddressFirst(), entity.getTblAddressSecond(), entity.getTblAddressThird()))
                            .genderStr(careRepository.findCalNameListStr(TblCareTopEnum.GENDER.getCareSeq(), entity.getGender()))
                            .careLevelStr(careRepository.findCalNameListStr(TblCareTopEnum.CARE_LEVEL.getCareSeq(), entity.getCareLevel()))
                            .workType(careRepository.findCalNameListStr(TblCareTopEnum.WORK_TYPE.getCareSeq(), entity.getInmateState()))
                            .build());
        }

        //4. 결과 반환
        return PatientResponseDto.GetRecruitList.builder()
                .totalPage(listPage.getTotalPages())
                .totalEle(listPage.getTotalElements())
                .hasNext(listPage.hasNext())
                .list(resultList)
                .build();
    }
}
