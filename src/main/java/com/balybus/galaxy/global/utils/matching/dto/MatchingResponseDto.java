package com.balybus.galaxy.global.utils.matching.dto;

import com.balybus.galaxy.domain.tblMatching.MatchState;
import com.balybus.galaxy.domain.tblMatching.TblMatching;
import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.patient.domain.TblPatientLog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MatchingResponseDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Score {
        private Long matchingSeq;
        private Long helperSeq;
        private Double totalScore;
        private Double locationScore;
        private Double timeScore;
        private Double dateScore;
        private Double workType;
        private Double welfare;
        private Double careLevel;
        private Double dementiaSymptom;
        private Double inmateState;
        private Double gender;
        private Double serviceMeal;
        private Double serviceMobility;
        private Double serviceToilet;
        private Double serviceDaily;
        private Integer helperExp;
        private Double certScore;
        private Integer wageScore;

        // DTO Projection을 위한 생성자 추가
        public Score(Long helperSeq, Double totalScore, Double locationScore, Double timeScore, Double dateScore,
                     Double workType, Double welfare, Double careLevel, Double dementiaSymptom, Double inmateState,
                     Double gender, Double serviceMeal, Double serviceMobility, Double serviceToilet, Double serviceDaily,
                     Integer helperExp, Double certScore, Integer wageScore) {
            this.helperSeq = helperSeq;
            this.totalScore = totalScore;
            this.locationScore = locationScore;
            this.timeScore = timeScore;
            this.dateScore = dateScore;
            this.workType = workType;
            this.welfare = welfare;
            this.careLevel = careLevel;
            this.dementiaSymptom = dementiaSymptom;
            this.inmateState = inmateState;
            this.gender = gender;
            this.serviceMeal = serviceMeal;
            this.serviceMobility = serviceMobility;
            this.serviceToilet = serviceToilet;
            this.serviceDaily = serviceDaily;
            this.helperExp = helperExp;
            this.certScore = certScore;
            this.wageScore = wageScore;
        }

        // toEntity
        public TblMatching toEntity(TblMatching bfMatching, TblPatientLog patientLog){
            return TblMatching.builder()
                    .id(bfMatching == null ? null : bfMatching.getId())
                    .patientLog(patientLog)
                    .helper(TblHelper.builder().id(this.helperSeq).build())
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