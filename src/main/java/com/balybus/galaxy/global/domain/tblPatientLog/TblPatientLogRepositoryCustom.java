package com.balybus.galaxy.global.domain.tblPatientLog;

import com.balybus.galaxy.global.domain.tblMatching.MatchState;
import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusResponseDto;

import java.util.List;

public interface TblPatientLogRepositoryCustom {
    List<MatchingStatusResponseDto.MatchingPatientInfo2> testQueryDSL(Long managerSeq, MatchState matchState);
}
