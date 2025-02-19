package com.balybus.galaxy.domain.tblMatching.dto;

import com.balybus.galaxy.domain.tblMatching.MatchState;
import com.balybus.galaxy.domain.tblMatching.TblMatching;
import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLog;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class MatchingResponseDto {

    @Getter
    @Setter
    @Builder
    public static class Score {
        private Long helperSeq;
        private BigDecimal totalScore;
        private BigDecimal locationScore;
        private BigDecimal timeScore;
        private BigDecimal dateScore;
        private BigDecimal workType;
        private BigDecimal welfare;
        private BigDecimal careLevel;
        private BigDecimal dementiaSymptom;
        private BigDecimal inmateState;
        private BigDecimal gender;
        private BigDecimal serviceMeal;
        private BigDecimal serviceMobility;
        private BigDecimal serviceToilet;
        private BigDecimal serviceDaily;
        private BigDecimal wageScore;
        private BigDecimal helperExp;
        private BigDecimal certScore;

        // toEntity
        public TblMatching toEntity(TblMatching bfMatching, TblHelper helperEntity, TblPatientLog patientLog){
            return TblMatching.builder()
                    .id(bfMatching == null ? null : bfMatching.getId())
                    .patientLog(patientLog)
                    .helper(helperEntity)
                    .totalScore(this.totalScore)
                    .locationScore(this.locationScore)
                    .timeScore(this.timeScore)
                    .dateScore(this.dateScore)
                    .workType(this.workType)
                    .welfare(this.welfare)
                    .careLevel(this.careLevel)
                    .dementiaSymptom(this.dementiaSymptom)
                    .inmateState(this.inmateState)
                    .gender(this.gender)
                    .serviceMeal(this.serviceMeal)
                    .serviceMobility(this.serviceMobility)
                    .serviceToilet(this.serviceToilet)
                    .serviceDaily(this.serviceDaily)
                    .helperExp(this.helperExp)
                    .certScore(this.certScore)
                    .wageScore(this.wageScore)
                    .matchState(bfMatching == null ? MatchState.INIT : bfMatching.getMatchState())
                    .useYn(true)
                    .build();
        }
    }
}