package com.balybus.galaxy.patient.matchingStatus.service;

import com.balybus.galaxy.careAssistant.domain.TblHelper;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.domain.tblMatching.MatchState;
import com.balybus.galaxy.global.domain.tblMatching.TblMatching;
import com.balybus.galaxy.global.domain.tblMatching.TblMatchingRepository;
import com.balybus.galaxy.global.domain.tblPatient.TblPatient;
import com.balybus.galaxy.global.domain.tblPatientLog.TblPatientLogRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.login.classic.service.loginAuth.LoginAuthCheckServiceImpl;
import com.balybus.galaxy.global.domain.tblPatientLog.TblPatientLog;
import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusRequestDto;
import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.balybus.galaxy.global.domain.tblMatching.MatchState.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingStatusServiceImpl implements MatchingStatusService{

    private final LoginAuthCheckServiceImpl loginAuthCheckService;
    private final TblMatchingRepository tblMatchingRepository;
    private final TblPatientLogRepository patientLogRepository;

    /**
     * 매칭 대기 어르신 정보 반환(관리자 입장)
     * @param userEmail 관리자 이메일
     * @return MatchingStatusResponseDto.MatchingPatientInfoList
     */
    @Override
    public MatchingStatusResponseDto.MatchingWaitPatientInfoList matchingWaitPatientInfoList(String userEmail) {
        // 매칭 대기 - 요양보호사 기준으로 전부 매칭 요청 전(INIT) 상태인 공고만

        //1. 관리자 로그인 유효성 확인
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        //2. 해당 관리자가 등록한 공고 중 전부 매칭 요청 전(INIT) 상태인 공고만 조회
        List<MatchingStatusResponseDto.MatchingPatientInfo> logResult = patientLogRepository.matchStatePatientLog(centerManager.getId(), INIT);

        //3. 공고별 매칭된 요양보호사 정보 조회
        List<MatchingStatusResponseDto.MatchingWaitPatientInfo> result = new ArrayList<>();
        for(MatchingStatusResponseDto.MatchingPatientInfo setData : logResult){
            List<MatchingStatusResponseDto.MatchedHelperInfo> data = tblMatchingRepository.findMatchingHelperInfo(setData.getPatientLogSeq());
            result.add(new MatchingStatusResponseDto.MatchingWaitPatientInfo(setData, data));
        }

        //3. dto 전환 및 반환
        return MatchingStatusResponseDto.MatchingWaitPatientInfoList.builder()
                .matchingPatientInfoList(result)
                .build();
    }

//    /**
//     * 매칭 중 어르신 정보 반환(관리자 입장)
//     * @param userEmail 관리자 이메일
//     * @return MatchingStatusResponseDto.MatchingPatientInfoList
//     */
//    @Override
//    public MatchingStatusResponseDto.MatchingPatientInfoList matchingPatientInfoList(String userEmail) {
//            return getPatientInfoListByMatchingState(userEmail, MATCH_REQUEST);
//    }

//    @Transactional(readOnly = true)
//    protected MatchingStatusResponseDto.MatchingPatientInfoList getPatientInfoListByMatchingState(String userEmail, MatchState matchState) {
//        // 1. 관리자 정보 조회
//        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);
//
//        // 2. 한 번의 쿼리로 모든 필요한 데이터 조회 (N+1 문제 해결)
//        List<TblMatching> allMatchings = tblMatchingRepository
//                .findMatchingByManagerIdAndMatchState(centerManager.getId(), matchState);
//
//        // 매칭중인 어르신 리스트(최종 반환 값)
//        List<MatchingStatusResponseDto.MatchingPatientInfo> matchingPatientInfoList = new ArrayList<>();
//
//        for(TblMatching matching : allMatchings) {
//            MatchingStatusResponseDto.MatchingPatientInfo matchingPatientInfo = getPatientInfo(matching);
//            matchingPatientInfoList.add(matchingPatientInfo);
//        }
//
//        return MatchingStatusResponseDto.MatchingPatientInfoList.builder()
//                .matchingPatientInfoList(matchingPatientInfoList)
//                .build();
//    }

//    /**
//     * 매칭 완료(수락) 및 매칭 거절 상태인 어르신 정보 리스트 반환(관리자 입장)
//     * @param userEmail 관리자 이메일
//     * @return MatchingStatusResponseDto.MatchedPatientInfoList
//    */
//    @Override
//    public MatchingStatusResponseDto.MatchedPatientInfoList matchedPatientInfoList(String userEmail) {
//        // 1. 관리자 정보 조회
//        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);
//
//        return getPatientList(centerManager);
//    }

//    @Transactional(readOnly = true)
//    protected MatchingStatusResponseDto.MatchedPatientInfoList getPatientList(TblCenterManager centerManager) {
//        // 2. 연관된 정보 모두 조회
//        List<MatchState> targetStates = Arrays.asList(MatchState.PERMIT_TUNE, MatchState.REJECT);
//        List<TblMatching> allMatchings = tblMatchingRepository.findMatchingByManagerIdAndMatchStates(centerManager.getId(), targetStates);
//
//        List<MatchingStatusResponseDto.MatchingPatientInfo> matchingPatientInfoList = new ArrayList<>();
//        List<MatchingStatusResponseDto.MatchingPatientInfo> matchingRejectedPatientInfoList = new ArrayList<>();
//
//        for(TblMatching matching : allMatchings) {
//            MatchingStatusResponseDto.MatchingPatientInfo matchingPatientInfo = getPatientInfo(matching);
//            if(matching.getMatchState().equals(MATCH_FIN)) {
//                matchingPatientInfoList.add(matchingPatientInfo);
//            }
//            else if(matching.getMatchState().equals(REJECT)) {
//                matchingRejectedPatientInfoList.add(matchingPatientInfo);
//            }
//        }
//
//        return MatchingStatusResponseDto.MatchedPatientInfoList.builder()
//                .matchedPatientInfoList(matchingPatientInfoList)
//                .matchingRejectedPatientInfoList(matchingRejectedPatientInfoList)
//                .build();
//    }
//
//    private MatchingStatusResponseDto.MatchingPatientInfo getPatientInfo(TblMatching matching) {
//        List<MatchingStatusResponseDto.MatchedHelperInfo> matchedHelperInfoList = new ArrayList<>();
//
//        TblHelper tblHelper = matching.getHelper();
//        matchedHelperInfoList.add(MatchingStatusResponseDto.MatchedHelperInfo.builder()
//                .helperSeq(tblHelper.getId())
//                .name(tblHelper.getName())
//                .gender(tblHelper.getGender())
//                .age(tblHelper.getBirthday())
//                .build());
//
//        // 2. 매칭 중인 어르신 정보
//        TblPatientLog ptLog = matching.getPatientLog();
//        return MatchingStatusResponseDto.MatchingPatientInfo.builder()
//                .patientSeq(ptLog.getPatient().getId())
//                .patientLogSeq(ptLog.getId())
//                .name(ptLog.getName())
//                .gender(ptLog.getGender())
//                .birthDate(ptLog.getBirthDate())
//                .workType(ptLog.getPatient().getWorkType())
//                .tblAddressFirst(ptLog.getTblAddressFirst().getName())
//                .tblAddressSecond(ptLog.getTblAddressSecond().getName())
//                .tblAddressThird(ptLog.getTblAddressThird().getName())
//                .matchedHelperInfos(matchedHelperInfoList)
//                .build();
//    }


    /**
     * 어르신 공고 매칭 상태 변경
     * @param userEmail String:로그인 사용자 이메일
     * @param dto MatchingStatusRequestDto.UpdatePatientMatchStatus: 상태 변경 값 및 완료의 경우, 매칭 상대 데이터 전달
     * @return MatchingStatusResponseDto.UpdatePatientMatchStatus
     */
    @Override
    @Transactional
    public MatchingStatusResponseDto.UpdatePatientMatchStatus updatePatientMatchStatus(String userEmail, MatchingStatusRequestDto.UpdatePatientMatchStatus dto) {
        //0. 로그인 사용자 유효성 검사
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        //1. dto 에서 매칭 id 로 매칭 정보 조회
        Optional<TblMatching> matchingOpt = tblMatchingRepository.findByPatientLog_idAndHelper_id(dto.getPatientLogSeq(), dto.getHelperSeq());
        if(matchingOpt.isEmpty()) throw new BadRequestException(ExceptionCode.INVALID_REQUEST);
        TblMatching matching = matchingOpt.get();

        //2. 로그인 사용자가 해당 매칭을 관리할 수 있는 관리자인지 유효성 검사
        TblPatientLog patientLog = matching.getPatientLog();
        TblPatient patient = loginAuthCheckService.checkPatientManagerAuth(patientLog.getPatient().getId(), centerManager.getId());

        //3. 공고의 매칭 상태 유효성 검사(해당 공고에 MATCH_FIN 인 결과가 있는지 확인)
        Optional<TblMatching> checkMatched = tblMatchingRepository.findByPatientLog_idAndMatchState(patientLog.getId(), MATCH_FIN);
        if(checkMatched.isPresent() && !checkMatched.get().getId().equals(matching.getId())){
            // 공고의 매칭 상태 == MATCH_FIN && 공고의 매칭 id != dto 매칭 id)
            return MatchingStatusResponseDto.UpdatePatientMatchStatus.builder()
                    .code(400)
                    .msg("FAIL::이미 매칭이 완료된 공고입니다.")
                    .build();
        } else {
            //4. dto 로 전달받은 매칭 상태로 값을 변경한다.
            matching.updateMatchState(dto.getMatchState());
            return MatchingStatusResponseDto.UpdatePatientMatchStatus.builder()
                    .code(200)
                    .msg("SUCCESS")
                    .build();
        }
    }
}
