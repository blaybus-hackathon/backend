package com.balybus.galaxy.patient.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

public class PatientResponseDto {
    @Getter
    @SuperBuilder
    public static class basicDto {
        private Long patientSeq;        // 어르신 정보 구분자 값
        private String managerEmail;    // 담당자 이메일
    }

    @Getter
    @SuperBuilder
    public static class SavePatientInfo extends basicDto { }

    @Getter
    @SuperBuilder
    public static class UpdatePatientInfo extends basicDto { }

    @Getter
    @Builder
    public static class RecruitHelper{
        private Long plSeq;         // 어르신 공고 구분자 값
        private Long patientSeq;    // 어르신 정보 구분자 값
        private String name;        // 이름
        private String birthYear;   // 생년월일 중 연도 반환
    }
}
