package com.balybus.galaxy.patient.basic.service;

import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;
import com.balybus.galaxy.global.domain.tblAddressSecond.TblAddressSecond;
import com.balybus.galaxy.global.domain.tblAddressThird.TblAddressThird;
import com.balybus.galaxy.global.domain.tblCare.TblCareRepository;
import com.balybus.galaxy.global.domain.tblCare.TblCareTopEnum;
import com.balybus.galaxy.global.domain.tblCare.service.TblCareServiceImpl;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.utils.address.service.serviceImpl.TblAddressFirstServiceImpl;
import com.balybus.galaxy.global.utils.address.service.serviceImpl.TblAddressSecondServiceImpl;
import com.balybus.galaxy.global.utils.address.service.serviceImpl.TblAddressThirdServiceImpl;
import com.balybus.galaxy.global.utils.code.CodeServiceImpl;
import com.balybus.galaxy.global.utils.file.FileService;
import com.balybus.galaxy.login.classic.service.loginAuth.LoginAuthCheckServiceImpl;
import com.balybus.galaxy.patient.basic.dto.BasicRequestDto;
import com.balybus.galaxy.patient.basic.dto.BasicResponseDto;
import com.balybus.galaxy.global.domain.tblPatient.TblPatient;
import com.balybus.galaxy.global.domain.tblPatient.TblPatientRepository;
import com.balybus.galaxy.global.domain.tblPatientTime.TblPatientTime;
import com.balybus.galaxy.global.domain.tblPatientTime.TblPatientTimeRepository;
import com.balybus.galaxy.patient.PatientBaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicServiceImpl implements BasicService{

    private final TblPatientRepository patientRepository;
    private final TblPatientTimeRepository patientTimeRepository;
    private final TblCareRepository careRepository;

    private final LoginAuthCheckServiceImpl loginAuthCheckService;
    private final CodeServiceImpl codeService;
    private final TblAddressFirstServiceImpl firstAddressService;
    private final TblAddressSecondServiceImpl secondAddressService;
    private final TblAddressThirdServiceImpl thirdAddressService;
    private final FileService fileService;
    private final TblCareServiceImpl careService;

    /**
     * 어르신 정보 등록
     * @param userEmail String:토큰 조회 결과 사용자 이메일 데이터
     * @param dto PatientRequestDto.SavePatientInfo
     * @return PatientResponseDto.SavePatientInfo
     */
    @Override
    @Transactional
    public BasicResponseDto.SavePatientInfo savePatientInfo(String userEmail, BasicRequestDto.SavePatientInfo dto) {
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
        return BasicResponseDto.SavePatientInfo.builder()
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
    public BasicResponseDto.UpdatePatientInfo updatePatientInfo(String userEmail, BasicRequestDto.UpdatePatientInfo dto) {
        //1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        //2. 어르신 정보 조회 (어르신 구분자 & 관리자 구분자) 및 수정
        //2-1. 어르신 데이터 조회
        TblPatient patient = loginAuthCheckService.checkPatientManagerAuth(dto.getPatientSeq(), centerManager.getId());

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
        return BasicResponseDto.UpdatePatientInfo.builder()
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
    public BasicResponseDto.GetOnePatientInfo getOnePatientInfo(String userEmail, Long patientSeq) {
        //1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        //2. 어르신 정보 조회 (어르신 구분자 & 관리자 구분자)
        TblPatient patient = loginAuthCheckService.checkPatientManagerAuth(patientSeq, centerManager.getId());

        //3. 어르신 돌봄 시간 요일 조회
        List<TblPatientTime> patientTimeList = patientTimeRepository.findByPatient_Id(patient.getId());

        //4. 반환 dto 생성
        BasicResponseDto.GetOnePatientInfo resultDto = new BasicResponseDto.GetOnePatientInfo(patient, patientTimeList);
        resultDto.setCareChoice(careService.getCareChoiceList(resultDto, false));
        resultDto.setCareBaseDtoNull();
        return resultDto;
    }

    @Override
    public BasicResponseDto.GetPatientList getPatientList(String userEmail, BasicRequestDto.GetPatientList dto) {
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
        List<BasicResponseDto.GetPatientListInfo> resultList = new ArrayList<>();
        for (TblPatient entity : patientEntityList) {
            resultList.add(
                    BasicResponseDto.GetPatientListInfo.builder()
                            .patientSeq(entity.getId())
                            .imgAddress(entity.getImg() == null ? null : fileService.getOneImgUrl(entity.getImg().getId()))
                            .name(entity.getName())
                            .age(codeService.calculateAge(LocalDate.parse(entity.getBirthDate(), java.time.format.DateTimeFormatter.BASIC_ISO_DATE)))
                            .address(codeService.fullAddressString(entity.getTblAddressFirst(), entity.getTblAddressSecond(), entity.getTblAddressThird()))
                            .genderStr(careRepository.findCalNameListStr(TblCareTopEnum.GENDER.getCareSeq(), entity.getGender()))
                            .careLevelStr(careRepository.findCalNameListStr(TblCareTopEnum.CARE_LEVEL.getCareSeq(), entity.getCareLevel()))
                            .workType(careRepository.findCalNameListStr(TblCareTopEnum.WORK_TYPE.getCareSeq(), entity.getInmateState()))
                            .build());
        }

        //4. 결과 반환
        return BasicResponseDto.GetPatientList.builder()
                .totalPage(listPage.getTotalPages())
                .totalEle(listPage.getTotalElements())
                .hasNext(listPage.hasNext())
                .list(resultList)
                .build();
    }
}
