package com.balybus.galaxy.patient.matchingStatus.service;

import com.balybus.galaxy.careAssistant.domain.TblHelper;
import com.balybus.galaxy.careAssistant.repository.HelperRepository;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.domain.tblMatching.MatchState;
import com.balybus.galaxy.global.domain.tblMatching.TblMatching;
import com.balybus.galaxy.global.domain.tblMatching.TblMatchingRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.login.classic.service.loginAuth.LoginAuthCheckServiceImpl;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLog;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLogRepository;
import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.balybus.galaxy.global.domain.tblMatching.MatchState.*;
import static com.balybus.galaxy.global.exception.ExceptionCode.NOT_FOUND_HELPER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingStatusServiceImpl implements MatchingStatusService{
    private final TblPatientLogRepository patientLogRepository;

    private final LoginAuthCheckServiceImpl loginAuthCheckService;
    private final TblMatchingRepository tblMatchingRepository;
    private final HelperRepository helperRepository;

    /**
     * 매칭중 어르신 정보 반환(관리자 입장)
     * @param userEmail 관리자 이메일
     * @return
     */
    @Override
    public MatchingStatusResponseDto.MatchingPatientInfoList matchingPatientInfoList(String userEmail) {
        // 1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        // 2. 연관된 어르신 정보 로그 테이블 모두 조회
        List<TblPatientLog> tblPatientLog = patientLogRepository.findAllByManagerId(centerManager.getId());

        // 매칭중인 어르신 리스트(최종 반환 값)
        List<MatchingStatusResponseDto.MatchingPatientInfo> matchingPatientInfoList = new ArrayList<>();

        for(TblPatientLog ptLog : tblPatientLog) {
            // 1. 어르신 매칭 테이블 반환(매칭 중 상태이며 해당 어르신 관계에 있는 테이블)
            List<TblMatching> tblMatching = tblMatchingRepository.findByPatientLog_idAndMatchState(ptLog.getId(), INIT);

            // 2. 매칭 중인 어르신의 요양 보호사 리스트
            List<MatchingStatusResponseDto.MatchedHelperInfo> matchedHelperInfoList = new ArrayList<>();

            // 3. 어르신 매칭 상태별 해당하는 요양 보호사 반환
            for(TblMatching matching : tblMatching) {
                // 4. 요양 보호사 검색
                TblHelper tblHelper = helperRepository.findById(matching.getHelper().getId())
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

                matchedHelperInfoList.add(MatchingStatusResponseDto.MatchedHelperInfo.builder()
                        .helperSeq(tblHelper.getId())
                        .name(tblHelper.getName())
                        .gender(tblHelper.getGender())
                        .age(tblHelper.getBirthday())
                        .build());
            }
            // 5. 매칭 중인 어르신 정보
            MatchingStatusResponseDto.MatchingPatientInfo matchingPatientInfo = MatchingStatusResponseDto.MatchingPatientInfo.builder()
                    .patientSeq(ptLog.getPatient().getId())
                    .name(ptLog.getName())
                    .gender(ptLog.getGender())
                    .birthDate(ptLog.getBirthDate())
                    .workType(ptLog.getPatient().getWorkType())
                    .tblAddressFirst(ptLog.getTblAddressFirst().getName())
                    .tblAddressSecond(ptLog.getTblAddressSecond().getName())
                    .tblAddressThird(ptLog.getTblAddressThird().getName())
                    .matchedHelperInfos(matchedHelperInfoList)
                    .build();
            matchingPatientInfoList.add(matchingPatientInfo);
        }

        return MatchingStatusResponseDto.MatchingPatientInfoList.builder()
                .matchingPatientInfoList(matchingPatientInfoList)
                .build();
    }

    /**
     * 매칭 완료(수락) 및 매칭 거절 상태인 어르신 정보 리스트 반환(관리자 입장)
     * @param userEmail 관리자 이메일
     * @return
     */
    @Override
    public MatchingStatusResponseDto.MatchedPatientInfoList matchedPatientInfoList(String userEmail) {
        // 1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        // 2. 연관된 어르신 정보 로그 테이블 모두 조회
        List<TblPatientLog> tblPatientLog = patientLogRepository.findAllByManagerId(centerManager.getId());

        // 매칭 완료(수락) 상태인 어르신 리스트(최종 반환 값)
        List<MatchingStatusResponseDto.MatchingPatientInfo> matchingPatientInfoList = getPatientListByMatchState(tblPatientLog, MATCH_FIN);

        // 매칭 거절 상태인 어르신 리스트(최종 반환 값)
        List<MatchingStatusResponseDto.MatchingPatientInfo> matchingRejectedHelperInfoList = getPatientListByMatchState(tblPatientLog, REJECT);

        return MatchingStatusResponseDto.MatchedPatientInfoList.builder()
                .matchedPatientInfoList(matchingPatientInfoList)
                .matchingRejectedPatientInfoList(matchingRejectedHelperInfoList)
                .build();
    }

    private List<MatchingStatusResponseDto.MatchingPatientInfo> getPatientListByMatchState(List<TblPatientLog> tblPatientLog, MatchState matchState) {
        List<MatchingStatusResponseDto.MatchingPatientInfo> matchingPatientInfoList = new ArrayList<>();

        for(TblPatientLog ptLog : tblPatientLog) {
            // 1. 어르신 매칭 테이블 반환(매칭 완료 상태이며 해당 어르신 관계에 있는 테이블)
            List<TblMatching> tblMatching = tblMatchingRepository.findByPatientLog_idAndMatchState(ptLog.getId(), matchState);

            // 2. 어르신의 요양 보호사 리스트
            List<MatchingStatusResponseDto.MatchedHelperInfo> matchedHelperInfoList = new ArrayList<>();

            // 3. 어르신 매칭 상태별 해당하는 요양 보호사 반환
            for(TblMatching matching : tblMatching) {
                // 4. 요양 보호사 검색
                TblHelper tblHelper = helperRepository.findById(matching.getHelper().getId())
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));
                matchedHelperInfoList.add(MatchingStatusResponseDto.MatchedHelperInfo.builder()
                        .helperSeq(tblHelper.getId())
                        .name(tblHelper.getName())
                        .gender(tblHelper.getGender())
                        .age(tblHelper.getBirthday())
                        .build());
            }
            // 5. 매칭 중인 어르신 정보
            if(!matchedHelperInfoList.isEmpty()) {
                MatchingStatusResponseDto.MatchingPatientInfo matchingPatientInfo = MatchingStatusResponseDto.MatchingPatientInfo.builder()
                        .patientSeq(ptLog.getPatient().getId())
                        .name(ptLog.getName())
                        .gender(ptLog.getGender())
                        .birthDate(ptLog.getBirthDate())
                        .workType(ptLog.getPatient().getWorkType())
                        .tblAddressFirst(ptLog.getTblAddressFirst().getName())
                        .tblAddressSecond(ptLog.getTblAddressSecond().getName())
                        .tblAddressThird(ptLog.getTblAddressThird().getName())
                        .matchedHelperInfos(matchedHelperInfoList)
                        .build();
                matchingPatientInfoList.add(matchingPatientInfo);
            }
        }
        return matchingPatientInfoList;
    }
}
