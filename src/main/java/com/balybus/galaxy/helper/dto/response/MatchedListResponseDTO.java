package com.balybus.galaxy.helper.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MatchedListResponseDTO {
    private Long matchedPatientId;
    private String patientName;
    private int patientGender;
    private String patientAge;
    private String managerPhoneNum;
    private int workType;
    private List<Long> addressIdList;
    private List<MatchedPatientResponseDTO.PatientWorkTime> patientWorkDays; // 희망근무시간
    private String diseases; // 보유 질병
    private List<Integer> careType; // 케어 필요 항목
    private String wage;
}
