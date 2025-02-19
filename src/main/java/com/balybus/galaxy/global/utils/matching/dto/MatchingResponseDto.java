package com.balybus.galaxy.global.utils.matching.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MatchingResponseDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Score {
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
    }
}