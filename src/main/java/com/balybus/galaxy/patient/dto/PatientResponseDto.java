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

    public static class UpdatePatientInfo{

    }

    public static class RecruitHelper{

    }
}
