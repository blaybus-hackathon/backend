package com.balybus.galaxy.patient.matchingStatus.dto;

import com.balybus.galaxy.global.domain.tblMatching.MatchState;
import lombok.Data;

public class MatchingStatusRequestDto {
    @Data
    public static class UpdatePatientMatchStatus {
        private Long patientLogSeq;     //매칭 어르신 공고 id
        private Long helperSeq;         //매칭 요양보호사 id
        private MatchState matchState;  //매칭 상태값
    }
}
