package com.balybus.galaxy.helper.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class MatchedPatientResponseDTO {
    private String patientName;
    private int patientGender;
    private String patientAge;
    private int workType;
    private List<String> addressList;
    private List<HelperWorkTime> helperWorkTimes; // 요양 보호사 희망근무시간
    private List<PatientWorkTime> patientWorkTimes;
    private String diseases; // 보유 질병
    private List<Integer> careType; // 케어 필요 항목
    private String wage;

    @Data
    @Builder
    public static class HelperWorkTime {
        private Integer day;
        private String startTime;
        private String endTime;
        private Boolean nego;
        private List<Integer> workTerm;
    }

    @Data
    @Builder
    public static class PatientWorkTime {
        private int day;
        private LocalTime startTime;
        private LocalTime endTime;
    }
}
