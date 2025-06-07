package com.balybus.galaxy.patient.matchingStatus.service;

import com.balybus.galaxy.careAssistant.domain.TblHelper;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.domain.tblMatching.MatchState;
import com.balybus.galaxy.global.domain.tblMatching.TblMatching;
import com.balybus.galaxy.global.domain.tblMatching.TblMatchingRepository;
import com.balybus.galaxy.login.classic.service.loginAuth.LoginAuthCheckServiceImpl;
import com.balybus.galaxy.global.domain.tblPatientLog.TblPatientLog;
import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.balybus.galaxy.global.domain.tblMatching.MatchState.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingStatusServiceImpl implements MatchingStatusService{

    private final LoginAuthCheckServiceImpl loginAuthCheckService;
    private final TblMatchingRepository tblMatchingRepository;

    /**
     * 매칭 대기 어르신 정보 반환(관리자 입장)
     * @param userEmail 관리자 이메일
     * @return MatchingStatusResponseDto.MatchingPatientInfoList
     */
    @Override
    public MatchingStatusResponseDto.MatchingPatientInfoList matchingWaitPatientInfoList(String userEmail) {
        return getPatientInfoListByMatchingState(userEmail, INIT);
    }

    /**
     * 매칭 중 어르신 정보 반환(관리자 입장)
     * @param userEmail 관리자 이메일
     * @return MatchingStatusResponseDto.MatchingPatientInfoList
     */
    @Override
    public MatchingStatusResponseDto.MatchingPatientInfoList matchingPatientInfoList(String userEmail) {
            return getPatientInfoListByMatchingState(userEmail, MATCH_REQUEST);
    }

    @Transactional(readOnly = true)
    protected MatchingStatusResponseDto.MatchingPatientInfoList getPatientInfoListByMatchingState(String userEmail, MatchState matchState) {
        // 1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        // 2. 한 번의 쿼리로 모든 필요한 데이터 조회 (N+1 문제 해결)
        List<TblMatching> allMatchings = tblMatchingRepository
                .findMatchingByManagerIdAndMatchState(centerManager.getId(), matchState);

        // 매칭중인 어르신 리스트(최종 반환 값)
        List<MatchingStatusResponseDto.MatchingPatientInfo> matchingPatientInfoList = new ArrayList<>();

        for(TblMatching matching : allMatchings) {
            MatchingStatusResponseDto.MatchingPatientInfo matchingPatientInfo = getPatientInfo(matching);
            matchingPatientInfoList.add(matchingPatientInfo);
        }

        return MatchingStatusResponseDto.MatchingPatientInfoList.builder()
                .matchingPatientInfoList(matchingPatientInfoList)
                .build();
    }

    /**
     * 매칭 완료(수락) 및 매칭 거절 상태인 어르신 정보 리스트 반환(관리자 입장)
     * @param userEmail 관리자 이메일
     * @return MatchingStatusResponseDto.MatchedPatientInfoList
    */
    @Override
    public MatchingStatusResponseDto.MatchedPatientInfoList matchedPatientInfoList(String userEmail) {
        // 1. 관리자 정보 조회
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        return getPatientList(centerManager);
    }

    @Transactional(readOnly = true)
    protected MatchingStatusResponseDto.MatchedPatientInfoList getPatientList(TblCenterManager centerManager) {
        // 2. 연관된 정보 모두 조회
        List<MatchState> targetStates = Arrays.asList(MatchState.PERMIT_TUNE, MatchState.REJECT);
        List<TblMatching> allMatchings = tblMatchingRepository.findMatchingByManagerIdAndMatchStates(centerManager.getId(), targetStates);

        List<MatchingStatusResponseDto.MatchingPatientInfo> matchingPatientInfoList = new ArrayList<>();
        List<MatchingStatusResponseDto.MatchingPatientInfo> matchingRejectedPatientInfoList = new ArrayList<>();

        for(TblMatching matching : allMatchings) {
            MatchingStatusResponseDto.MatchingPatientInfo matchingPatientInfo = getPatientInfo(matching);
            if(matching.getMatchState().equals(MATCH_FIN)) {
                matchingPatientInfoList.add(matchingPatientInfo);
            }
            else if(matching.getMatchState().equals(REJECT)) {
                matchingRejectedPatientInfoList.add(matchingPatientInfo);
            }
        }

        return MatchingStatusResponseDto.MatchedPatientInfoList.builder()
                .matchedPatientInfoList(matchingPatientInfoList)
                .matchingRejectedPatientInfoList(matchingRejectedPatientInfoList)
                .build();
    }

    private MatchingStatusResponseDto.MatchingPatientInfo getPatientInfo(TblMatching matching) {
        List<MatchingStatusResponseDto.MatchedHelperInfo> matchedHelperInfoList = new ArrayList<>();

        TblHelper tblHelper = matching.getHelper();
        matchedHelperInfoList.add(MatchingStatusResponseDto.MatchedHelperInfo.builder()
                .helperSeq(tblHelper.getId())
                .name(tblHelper.getName())
                .gender(tblHelper.getGender())
                .age(tblHelper.getBirthday())
                .build());

        // 2. 매칭 중인 어르신 정보
        TblPatientLog ptLog = matching.getPatientLog();
        return MatchingStatusResponseDto.MatchingPatientInfo.builder()
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
    }
}
