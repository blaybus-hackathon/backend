package com.balybus.galaxy.patient.dto;

import lombok.Builder;
import lombok.Getter;

public class PatientResponseDto {
    @Getter
    @Builder
    public static class SavePatientInfo{
        private Long patientSeq;    // 어르신 정보 구분자 값
        private String name;        // 이름
        private String birthYear;   // 생년월일 중 연도 반환
    }

    @Getter
    @Builder
    public static class UpdatePatientInfo{
        private Long patientSeq;    // 어르신 정보 구분자 값
        private String name;        // 이름
        private String birthYear;   // 생년월일 중 연도 반환
    }

    @Getter
    @Builder
    public static class RecruitHelper{
        private Long plSeq;         // 어르신 공고 구분자 값
        private Long patientSeq;    // 어르신 정보 구분자 값
        private String name;        // 이름
        private String birthYear;   // 생년월일 중 연도 반환
    }
}
