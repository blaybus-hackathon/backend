package com.balybus.galaxy.patient.matchingStatus.service;

import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusRequestDto;
import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusResponseDto;

public interface MatchingStatusService {
    MatchingStatusResponseDto.MatchingStatusPatientInfoList matchingWaitPatientInfoList(String userEmail); // 어르신 매칭 대기 리스트 반환
    MatchingStatusResponseDto.MatchingStatusPatientInfoList matchingPatientInfoList(String userEmail); // 어르신 매칭중 리스트 반화
    MatchingStatusResponseDto.MatchingStatusPatientInfoList matchedFinPatientInfoList(String userEmail); // 어르신 매칭 완료 리스트 반화
    MatchingStatusResponseDto.UpdatePatientMatchStatus updatePatientMatchStatus(String userEmail, MatchingStatusRequestDto.UpdatePatientMatchStatus dto); // 어르신 공고 매칭 상태 변경
    
    // 요양보호사 기준 매칭 목록 조회 (JWT 토큰 기반)
    MatchingStatusResponseDto.HelperMatchingList helperMatchingRequestList(String userEmail); // 요양보호사 매칭 요청 목록 조회 (요청/조율)
    MatchingStatusResponseDto.HelperMatchingList helperMatchingCompletedList(String userEmail); // 요양보호사 매칭 완료 목록 조회 (매칭 완료)
    
    // 요양보호사 구분자 기반 매칭 목록 조회
    MatchingStatusResponseDto.HelperMatchingList helperMatchingRequestListByHelperId(Long helperId); // 요양보호사 구분자로 매칭 요청 목록 조회 (요청/조율)
    MatchingStatusResponseDto.HelperMatchingList helperMatchingCompletedListByHelperId(Long helperId); // 요양보호사 구분자로 매칭 완료 목록 조회 (매칭 완료)
}
