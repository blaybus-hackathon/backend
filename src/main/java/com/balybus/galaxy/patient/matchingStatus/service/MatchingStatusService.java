package com.balybus.galaxy.patient.matchingStatus.service;

import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusRequestDto;
import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusResponseDto;

public interface MatchingStatusService {
    MatchingStatusResponseDto.MatchingWaitPatientInfoList matchingWaitPatientInfoList(String userEmail); // 어르신 매칭 대기 리스트 반환
//    MatchingStatusResponseDto.MatchingPatientInfoList matchingPatientInfoList(String userEmail); // 어르신 매칭중 리스트 반화
//    MatchingStatusResponseDto.MatchedPatientInfoList matchedPatientInfoList(String userEmail); // 어르신 매칭 완료 리스트 반화
    MatchingStatusResponseDto.UpdatePatientMatchStatus updatePatientMatchStatus(String userEmail, MatchingStatusRequestDto.UpdatePatientMatchStatus dto); // 어르신 공고 매칭 상태 변경
}
