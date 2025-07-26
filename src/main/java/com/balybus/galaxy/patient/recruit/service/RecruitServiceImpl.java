package com.balybus.galaxy.patient.recruit.service;

import com.balybus.galaxy.careAssistant.domain.TblHelper;
import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;
import com.balybus.galaxy.global.domain.tblAddressSecond.TblAddressSecond;
import com.balybus.galaxy.global.domain.tblAddressThird.TblAddressThird;
import com.balybus.galaxy.global.domain.tblCare.TblCareRepository;
import com.balybus.galaxy.global.domain.tblCare.TblCareTopEnum;
import com.balybus.galaxy.global.domain.tblCare.service.TblCareServiceImpl;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.domain.tblMatching.MatchingServiceImpl;
import com.balybus.galaxy.global.domain.tblMatching.TblMatching;
import com.balybus.galaxy.global.domain.tblMatching.TblMatchingRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.address.service.serviceImpl.TblAddressFirstServiceImpl;
import com.balybus.galaxy.global.utils.address.service.serviceImpl.TblAddressSecondServiceImpl;
import com.balybus.galaxy.global.utils.address.service.serviceImpl.TblAddressThirdServiceImpl;
import com.balybus.galaxy.global.utils.code.CodeServiceImpl;
import com.balybus.galaxy.global.utils.file.FileService;
import com.balybus.galaxy.login.classic.service.loginAuth.LoginAuthCheckServiceImpl;
import com.balybus.galaxy.global.domain.tblPatient.TblPatient;
import com.balybus.galaxy.global.domain.tblPatientLog.TblPatientLog;
import com.balybus.galaxy.global.domain.tblPatientLog.TblPatientLogRepository;
import com.balybus.galaxy.global.domain.tblPatientTime.TblPatientTime;
import com.balybus.galaxy.global.domain.tblPatientTime.TblPatientTimeRepository;
import com.balybus.galaxy.global.domain.tblPatientTimeLog.TblPatientTimeLog;
import com.balybus.galaxy.global.domain.tblPatientTimeLog.TblPatientTimeLogRepository;
import com.balybus.galaxy.patient.PatientBaseDto;
import com.balybus.galaxy.patient.recruit.dto.RecruitRequestDto;
import com.balybus.galaxy.patient.recruit.dto.RecruitResponseDto;
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
public class RecruitServiceImpl implements RecruitService {
    private final TblPatientTimeRepository patientTimeRepository;
    private final TblPatientLogRepository patientLogRepository;
    private final TblPatientTimeLogRepository patientTimeLogRepository;
    private final TblCareRepository careRepository;
    private final TblMatchingRepository matchingRepository;

    private final LoginAuthCheckServiceImpl loginAuthCheckService;
    private final MatchingServiceImpl matchingService;
    private final CodeServiceImpl codeService;
    private final TblAddressFirstServiceImpl firstAddressService;
    private final TblAddressSecondServiceImpl secondAddressService;
    private final TblAddressThirdServiceImpl thirdAddressService;
    private final FileService fileService;
    private final TblCareServiceImpl careService;


    /**
     * 어르신 공고 등록
     * @param userDetails UserDetails:토큰 조회 결과 데이터
     * @param dto PatientRequestDto.RecruitHelper
     * @return PatientResponseDto.RecruitHelper
     */
    @Override
    @Transactional
    public RecruitResponseDto.RecruitHelper recruitHelper(UserDetails userDetails, RecruitRequestDto.RecruitHelper dto) {
        //1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userDetails.getUsername());

        //2. 어르신 정보 조회 (어르신 구분자 & 관리자 구분자) 및 수정 & 로그 등록
        //2-1. 어르신 데이터 조회
        TblPatient patient = loginAuthCheckService.checkPatientManagerAuth(dto.getPatientSeq(), centerManager.getId());

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

        return RecruitResponseDto.RecruitHelper.builder()
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
    public RecruitResponseDto.GetRecruitList getRecruitList(String userEmail, RecruitRequestDto.GetRecruitList dto) {
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
        List<RecruitResponseDto.GetRecruitListInfo> resultList = new ArrayList<>();
        for (TblPatientLog entity : patientEntityList) {
            resultList.add(
                    RecruitResponseDto.GetRecruitListInfo.builder()
                            .patientLogSeq(entity.getId())
                            .imgAddress(entity.getPatient().getImg() == null ? null : fileService.getOneImgUrl(entity.getPatient().getImg().getId()))
                            .name(entity.getName())
                            .age(codeService.calculateAge(LocalDate.parse(entity.getBirthDate(), java.time.format.DateTimeFormatter.BASIC_ISO_DATE)))
                            .address(codeService.fullAddressString(entity.getTblAddressFirst(), entity.getTblAddressSecond(), entity.getTblAddressThird()))
                            .genderStr(careRepository.findCalNameListStr(TblCareTopEnum.GENDER.getCareSeq(), entity.getGender()))
                            .careLevelStr(careRepository.findCalNameListStr(TblCareTopEnum.CARE_LEVEL.getCareSeq(), entity.getCareLevel()))
                            .workType(careRepository.findCalNameListStr(TblCareTopEnum.WORK_TYPE.getCareSeq(), entity.getInmateState()))
                            .build());
        }

        //4. 결과 반환
        return RecruitResponseDto.GetRecruitList.builder()
                .totalPage(listPage.getTotalPages())
                .totalEle(listPage.getTotalElements())
                .hasNext(listPage.hasNext())
                .list(resultList)
                .build();
    }


    @Override
    public RecruitResponseDto.GetRecruitList getRecruitPersonalList(String userEmail, RecruitRequestDto.GetRecruitPersonalList dto) {
        //1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        //2. 해당 관리자가 관리중인 어르신 공고 리스트 조회
        Pageable page = PageRequest.of(
                dto.getPageNo()==null ? 0 : dto.getPageNo()
                , dto.getPageSize()==null ? 10 : dto.getPageSize()
                , Sort.by(Sort.Order.asc("name"), Sort.Order.desc("birthDate"), Sort.Order.desc("id")));
        Page<TblPatientLog> listPage = patientLogRepository.findByPatientIdAndManagerId(dto.getPatientSeq(), centerManager.getId(), page);
        List<TblPatientLog> patientEntityList = listPage.getContent();

        //3. 성별/근무종류/주소지/장기요양등급 각 항목 이름 조회 및 dto 리스트 정리
        List<RecruitResponseDto.GetRecruitListInfo> resultList = new ArrayList<>();
        for (TblPatientLog entity : patientEntityList) {
            resultList.add(
                    RecruitResponseDto.GetRecruitListInfo.builder()
                            .patientLogSeq(entity.getId())
                            .imgAddress(entity.getPatient().getImg() == null ? null : fileService.getOneImgUrl(entity.getPatient().getImg().getId()))
                            .name(entity.getName())
                            .age(codeService.calculateAge(LocalDate.parse(entity.getBirthDate(), java.time.format.DateTimeFormatter.BASIC_ISO_DATE)))
                            .address(codeService.fullAddressString(entity.getTblAddressFirst(), entity.getTblAddressSecond(), entity.getTblAddressThird()))
                            .genderStr(careRepository.findCalNameListStr(TblCareTopEnum.GENDER.getCareSeq(), entity.getGender()))
                            .careLevelStr(careRepository.findCalNameListStr(TblCareTopEnum.CARE_LEVEL.getCareSeq(), entity.getCareLevel()))
                            .workType(careRepository.findCalNameListStr(TblCareTopEnum.WORK_TYPE.getCareSeq(), entity.getInmateState()))
                            .build());
        }

        //4. 결과 반환
        return RecruitResponseDto.GetRecruitList.builder()
                .totalPage(listPage.getTotalPages())
                .totalEle(listPage.getTotalElements())
                .hasNext(listPage.hasNext())
                .list(resultList)
                .build();
    }

    /**
     * 어르신 정보 상세 조회
     * @param userEmail String:토큰 조회 결과 사용자 이메일 데이터
     * @param patientLogSeq Long:어르신 공고 구분자
     * @return PatientResponseDto.GetOnePatientInfo
     */
    @Override
    public RecruitResponseDto.GetOneRecruitPatientInfo getOneRecruitPatientInfo(String userEmail, Long patientLogSeq, boolean managerYn) {
        //1. 어르신 로그 정보 조회
        TblPatientLog patientLog = getPatientLog(patientLogSeq);

        //2. 로그인 정보 조회
        if(managerYn) {
            //2-1-1. 관리자 정보 조회
            TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);
            //2-1-2. 어르신 정보 조회 가능 여부 확인(어르신 구분자 & 관리자 구분자)
            TblPatient patient = loginAuthCheckService.checkPatientManagerAuth(patientLog.getPatient().getId(), centerManager.getId());
        } else {
            //2-2-1. 요양보호사 정보 조회
            TblHelper helper = loginAuthCheckService.checkHelper(userEmail);

            //2-2-2. 어르신 정보 조회 가능 여부 확인(매칭 확인 - 요양보호사)
            Optional<TblMatching> matchingOpt = matchingRepository.findByPatientLog_idAndHelper_id(patientLogSeq, helper.getId());
            if(matchingOpt.isEmpty()) throw new BadRequestException(ExceptionCode.NOT_FOUND_PATIENT_RECRUIT);
        }

        //3. 어르신 돌봄 시간 요일 조회
        List<TblPatientTimeLog> patientTimeLogList = patientTimeLogRepository.findByPatientLog_Id(patientLog.getId());

        //4. 반환 dto 생성
        RecruitResponseDto.GetOneRecruitPatientInfo resultDto = new RecruitResponseDto.GetOneRecruitPatientInfo(patientLog, patientTimeLogList);
        resultDto.setCareChoice(careService.getCareChoiceList(resultDto, true));
        resultDto.setCareBaseDtoNull();
        return resultDto;
    }

    /**
     * 어르신 로그 정보 조회
     * @param patientLogSeq 어르신로그(공고) 구분자
     * @return TblPatientLog 엔티티
     */
    private TblPatientLog getPatientLog(Long patientLogSeq){
        Optional<TblPatientLog> patientLogOpt = patientLogRepository.findById(patientLogSeq);
        if(patientLogOpt.isEmpty()) throw new BadRequestException(ExceptionCode.NOT_FOUND_PATIENT_RECRUIT);
        return patientLogOpt.get();
    }


    /**
     * 어르신 공고 등록
     * @param userName String:토큰 조회 결과 데이터
     * @param dto PatientRequestDto.RecruitHelper
     * @return PatientResponseDto.RecruitHelper
     */
    @Override
    @Transactional
    public RecruitResponseDto.UpdateRecruitPatientInfo updateRecruitPatientInfo(String userName, RecruitRequestDto.UpdateRecruitPatientInfo dto) {
        //1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userName);

        //2. 어르신 정보 조회 (어르신 구분자 & 관리자 구분자) 및 수정 & 로그 등록
        //2-1. 어르신 로그 정보 조회
        Optional<TblPatientLog> patientLogOpt = patientLogRepository.findById(dto.getPatientLogSeq());
        if(patientLogOpt.isEmpty()) throw new BadRequestException(ExceptionCode.NOT_FOUND_PATIENT_RECRUIT);
        TblPatientLog patientLog = patientLogOpt.get();

        //2-2. 어르신 정보 조회 (어르신 구분자 & 관리자 구분자)
        TblPatient patient = loginAuthCheckService.checkPatientManagerAuth(patientLog.getPatient().getId(), centerManager.getId());

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

        //2-5. 어르신 로그 entity 수정
        patientLog.basicUpdate(dto,
                calWage.get("timeWage"), calWage.get("dayWage"), calWage.get("weekWage"),
                firstAddress, secondAddress, thirdAddress);

        //3-1. TblPatientTimeLog 어르신 돌봄 시간 요일(리스트 정보) 조회 및 삭제
        List<TblPatientTimeLog> patientTimeLogList = patientTimeLogRepository.findByPatientLog_Id(patientLog.getId());
        patientTimeLogRepository.deleteAll(patientTimeLogList);
        //3-2. TblPatientTimeLog 어르신 돌봄 시간 요일(리스트 정보) entity 전환 및 저장
        List<TblPatientTimeLog> savePatientTimeLogList = new ArrayList<>();
        for(PatientBaseDto.SavePatientTimeInfo ptDto : dto.getTimeList()){
            savePatientTimeLogList.add(ptDto.toLogEntity(patientLog));
        }
        patientTimeLogRepository.saveAll(savePatientTimeLogList);

        //4. 요양보호사 추천 리스트 매칭
        if(dto.isReMatchYn()) matchingService.matchingSystem(patientLog.getId());

        return RecruitResponseDto.UpdateRecruitPatientInfo.builder()
                .plSeq(patientLog.getId())
                .patientSeq(patient.getId())
                .name(patient.getName())
                .birthYear(patient.getBirthDate().substring(0, 4))
                .build();
    }
}
