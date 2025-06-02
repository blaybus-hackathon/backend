package com.balybus.galaxy.patient.matchingStatus.service;

import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusResponseDto;

public interface MatchingStatusService {
    MatchingStatusResponseDto.MatchingPatientInfoList matchingPatientInfoList(String userEmail); // 어르신 매칭중 리스트 반화
    MatchingStatusResponseDto.MatchedPatientInfoList matchedPatientInfoList(String userEmail); // 어르신 매칭 완료 리스트 반화
}
