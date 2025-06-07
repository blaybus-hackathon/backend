package com.balybus.galaxy.patient.matchingStatus.dto;

import com.balybus.galaxy.global.domain.tblMatching.MatchState;
import lombok.Data;

public class MatchingStatusRequestDto {
    @Data
    public static class UpdatePatientMatchStatus {
        private Long matchingId;    //매칭 id
        private MatchState matchState;  //매칭 상태값
    }
}
