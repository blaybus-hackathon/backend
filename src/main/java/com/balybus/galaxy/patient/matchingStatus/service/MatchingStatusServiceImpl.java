package com.balybus.galaxy.patient.matchingStatus.service;

import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.domain.tblMatching.MatchState;
import com.balybus.galaxy.global.domain.tblMatching.SelectMatchStatus;
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
import com.balybus.galaxy.careAssistant.domain.TblHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.balybus.galaxy.global.domain.tblMatching.MatchState.*;
import static com.balybus.galaxy.global.domain.tblMatching.SelectMatchStatus.*;

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
     * @return MatchingStatusResponseDto.MatchingStatusPatientInfoList<MatchingStatusResponseDto.MatchingWaitPatientInfo>
     */
    @Override
    public MatchingStatusResponseDto.MatchingStatusPatientInfoList matchingWaitPatientInfoList(String userEmail) {
        // 매칭 대기 - 요양보호사 기준으로 전부 매칭 요청 전(INIT) 상태인 공고만
        return matchingStatusPatientInfoList(userEmail, ONLY_SELECT_INIT);
    }

    /**
     * 매칭 중 어르신 정보 반환(관리자 입장)
     * @param userEmail 관리자 이메일
     * @return MatchingStatusResponseDto.MatchingPatientInfoList
     */
    @Override
    public MatchingStatusResponseDto.MatchingStatusPatientInfoList matchingPatientInfoList(String userEmail) {
        // 진행중 - 매칭 대기와 완료 이외의 모든 공고
        // 채팅하기(조율중) | 매칭 요청 | 매칭 요청 전 | 응답거절 로 묶어서 데이터 반환
        return matchingStatusPatientInfoList(userEmail, ONLY_SELECT_MATCHING);
    }

    /**
     * 매칭 완료(수락) 및 매칭 거절 상태인 어르신 정보 리스트 반환(관리자 입장)
     * @param userEmail 관리자 이메일
     * @return MatchingStatusResponseDto.MatchingStatusPatientInfoList<?>
    */
    @Override
    public MatchingStatusResponseDto.MatchingStatusPatientInfoList matchedFinPatientInfoList(String userEmail) {
        // 완료 - 요양보호사 기준으로 단 한명이라도 (MATCH_FIN) 상태인 공고들 모음
        // 매칭 완료 | 채팅하기(조율중) | 매칭 요청 | 매칭 요청 전 | 응답거절 로 묶어서 데이터 반환
        return matchingStatusPatientInfoList(userEmail, ONLY_SELECT_MATCH_FIN);
    }

    private MatchingStatusResponseDto.MatchingStatusPatientInfoList matchingStatusPatientInfoList(String userEmail, SelectMatchStatus selectMatchStatus){
        //1. 관리자 로그인 유효성 확인
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        //2. 해당 관리자가 등록한 공고 중 MATCH_FIN 상태가 존재하는 공고만 조회
        List<MatchingStatusResponseDto.MatchingPatientInfo> logResult = patientLogRepository.matchStatePatientLog(centerManager.getId(), selectMatchStatus);

        //3. 공고별 매칭된 요양보호사 정보 조회
        List<MatchingStatusResponseDto.MatchedFinPatientInfo> result = new ArrayList<>();
        for(MatchingStatusResponseDto.MatchingPatientInfo setData : logResult){
            // 매칭 완료
            List<MatchingStatusResponseDto.MatchedHelperInfo> matchFinHelperInfoList = null;
            // 채팅하기(조율중)
            List<MatchingStatusResponseDto.MatchedHelperInfo> permitTuneHelperInfoList = null;
            // 매칭 요청
            List<MatchingStatusResponseDto.MatchedHelperInfo> matchRequestHelperInfoList = null;
            // 응답거절
            List<MatchingStatusResponseDto.MatchedHelperInfo> rejectHelperInfoList = null;
            // 매칭 요청 전
            List<MatchingStatusResponseDto.MatchedHelperInfo> initHelperInfoList = null;

            for (MatchState state : selectMatchStatus.getIncludedStates()) {
                List<MatchingStatusResponseDto.MatchedHelperInfo> data = tblMatchingRepository.findMatchingHelperInfo(setData.getPatientLogSeq(), state);

                switch (state) {
                    case MATCH_FIN -> matchFinHelperInfoList = data;
                    case PERMIT_TUNE -> permitTuneHelperInfoList = data;
                    case MATCH_REQUEST -> matchRequestHelperInfoList = data;
                    case REJECT -> rejectHelperInfoList = data;
                    case INIT -> initHelperInfoList = data;
                }
            }

            result.add(new MatchingStatusResponseDto.MatchedFinPatientInfo(setData
                    , matchFinHelperInfoList
                    , permitTuneHelperInfoList
                    , matchRequestHelperInfoList
                    , initHelperInfoList
                    , rejectHelperInfoList));
        }

        return MatchingStatusResponseDto.MatchingStatusPatientInfoList.builder()
                .list(result)
                .build();
    }

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

    /**
     * 요양보호사 매칭 요청 목록 조회 (요청/조율)
     * @param userEmail 요양보호사 이메일
     * @return MatchingStatusResponseDto.HelperMatchingList
     */
    @Override
    public MatchingStatusResponseDto.HelperMatchingList helperMatchingRequestList(String userEmail) {
        //1. 요양보호사 로그인 유효성 확인
        TblHelper helper = loginAuthCheckService.checkHelper(userEmail);

        //2. 해당 요양보호사의 매칭 요청/조율 상태 조회 (MATCH_REQUEST, PERMIT_TUNE)
        List<MatchState> matchStates = Arrays.asList(MATCH_REQUEST, PERMIT_TUNE);
        List<TblMatching> matchings = tblMatchingRepository.findMatchingByHelperIdAndMatchStates(helper.getId(), matchStates);

        //3. 매칭 정보를 DTO로 변환
        List<MatchingStatusResponseDto.HelperMatchingInfo> result = matchings.stream()
                .map(matching -> {
                    TblPatientLog patientLog = matching.getPatientLog();
                    return new MatchingStatusResponseDto.HelperMatchingInfo(
                            patientLog.getPatient().getId(),
                            patientLog.getId(),
                            patientLog.getName(),
                            patientLog.getBirthDate(),
                            String.valueOf(patientLog.getGender()),
                            String.valueOf(patientLog.getWorkType()),
                            String.valueOf(patientLog.getCareLevel()),
                            patientLog.getTblAddressFirst().getName(),
                            patientLog.getTblAddressSecond().getName(),
                            patientLog.getTblAddressThird().getName(),
                            matching.getMatchState().name(),
                            matching.getMatchState().getMsg()
                    );
                })
                .collect(Collectors.toList());

        return MatchingStatusResponseDto.HelperMatchingList.builder()
                .list(result)
                .build();
    }

    /**
     * 요양보호사 매칭 완료 목록 조회 (매칭 완료)
     * @param userEmail 요양보호사 이메일
     * @return MatchingStatusResponseDto.HelperMatchingList
     */
    @Override
    public MatchingStatusResponseDto.HelperMatchingList helperMatchingCompletedList(String userEmail) {
        //1. 요양보호사 로그인 유효성 확인
        TblHelper helper = loginAuthCheckService.checkHelper(userEmail);

        //2. 해당 요양보호사의 매칭 완료 상태 조회 (MATCH_FIN)
        List<TblMatching> matchings = tblMatchingRepository.findMatchingByHelperIdAndMatchState(helper.getId(), MATCH_FIN);

        //3. 매칭 정보를 DTO로 변환
        List<MatchingStatusResponseDto.HelperMatchingInfo> result = matchings.stream()
                .map(matching -> {
                    TblPatientLog patientLog = matching.getPatientLog();
                    return new MatchingStatusResponseDto.HelperMatchingInfo(
                            patientLog.getPatient().getId(),
                            patientLog.getId(),
                            patientLog.getName(),
                            patientLog.getBirthDate(),
                            String.valueOf(patientLog.getGender()),
                            String.valueOf(patientLog.getWorkType()),
                            String.valueOf(patientLog.getCareLevel()),
                            patientLog.getTblAddressFirst().getName(),
                            patientLog.getTblAddressSecond().getName(),
                            patientLog.getTblAddressThird().getName(),
                            matching.getMatchState().name(),
                            matching.getMatchState().getMsg()
                    );
                })
                .collect(Collectors.toList());

        return MatchingStatusResponseDto.HelperMatchingList.builder()
                .list(result)
                .build();
    }

    /**
     * 요양보호사 구분자로 매칭 요청 목록 조회 (요청/조율)
     * @param helperId 요양보호사 구분자
     * @return MatchingStatusResponseDto.HelperMatchingList
     */
    @Override
    public MatchingStatusResponseDto.HelperMatchingList helperMatchingRequestListByHelperId(Long helperId) {
        //1. 해당 요양보호사의 매칭 요청/조율 상태 조회 (MATCH_REQUEST, PERMIT_TUNE)
        List<MatchState> matchStates = Arrays.asList(MATCH_REQUEST, PERMIT_TUNE);
        List<TblMatching> matchings = tblMatchingRepository.findMatchingByHelperIdAndMatchStates(helperId, matchStates);

        //2. 매칭 정보를 DTO로 변환
        List<MatchingStatusResponseDto.HelperMatchingInfo> result = matchings.stream()
                .map(matching -> {
                    TblPatientLog patientLog = matching.getPatientLog();
                    return new MatchingStatusResponseDto.HelperMatchingInfo(
                            patientLog.getPatient().getId(),
                            patientLog.getId(),
                            patientLog.getName(),
                            patientLog.getBirthDate(),
                            String.valueOf(patientLog.getGender()),
                            String.valueOf(patientLog.getWorkType()),
                            String.valueOf(patientLog.getCareLevel()),
                            patientLog.getTblAddressFirst().getName(),
                            patientLog.getTblAddressSecond().getName(),
                            patientLog.getTblAddressThird().getName(),
                            matching.getMatchState().name(),
                            matching.getMatchState().getMsg()
                    );
                })
                .collect(Collectors.toList());

        return MatchingStatusResponseDto.HelperMatchingList.builder()
                .list(result)
                .build();
    }

    /**
     * 요양보호사 구분자로 매칭 완료 목록 조회 (매칭 완료)
     * @param helperId 요양보호사 구분자
     * @return MatchingStatusResponseDto.HelperMatchingList
     */
    @Override
    public MatchingStatusResponseDto.HelperMatchingList helperMatchingCompletedListByHelperId(Long helperId) {
        //1. 해당 요양보호사의 매칭 완료 상태 조회 (MATCH_FIN)
        List<TblMatching> matchings = tblMatchingRepository.findMatchingByHelperIdAndMatchState(helperId, MATCH_FIN);

        //2. 매칭 정보를 DTO로 변환
        List<MatchingStatusResponseDto.HelperMatchingInfo> result = matchings.stream()
                .map(matching -> {
                    TblPatientLog patientLog = matching.getPatientLog();
                    return new MatchingStatusResponseDto.HelperMatchingInfo(
                            patientLog.getPatient().getId(),
                            patientLog.getId(),
                            patientLog.getName(),
                            patientLog.getBirthDate(),
                            String.valueOf(patientLog.getGender()),
                            String.valueOf(patientLog.getWorkType()),
                            String.valueOf(patientLog.getCareLevel()),
                            patientLog.getTblAddressFirst().getName(),
                            patientLog.getTblAddressSecond().getName(),
                            patientLog.getTblAddressThird().getName(),
                            matching.getMatchState().name(),
                            matching.getMatchState().getMsg()
                    );
                })
                .collect(Collectors.toList());

        return MatchingStatusResponseDto.HelperMatchingList.builder()
                .list(result)
                .build();
    }
}
