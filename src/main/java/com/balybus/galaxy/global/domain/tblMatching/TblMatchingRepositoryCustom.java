package com.balybus.galaxy.global.domain.tblMatching;

import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusResponseDto;

import java.util.List;

public interface TblMatchingRepositoryCustom {
    List<MatchingStatusResponseDto.MatchedHelperInfo> findMatchingHelperInfo(Long patientLogId);
}
