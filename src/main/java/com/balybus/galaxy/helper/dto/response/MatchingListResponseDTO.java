package com.balybus.galaxy.helper.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MatchingListResponseDTO {
    private Long patientId; // 어르신 ID(자세히 보기 조회시 사용)
    private String patientName;
    private int patientGender;
    private String patientAge;
    private int workCategory; // 근무 종류
    private List<Long> patientAddressId;
}
