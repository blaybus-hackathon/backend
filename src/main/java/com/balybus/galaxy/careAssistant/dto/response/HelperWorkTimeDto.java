package com.balybus.galaxy.careAssistant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HelperWorkTimeDto {
    private Long id;
    private Integer date; // 근무 가능 요일
    private String startTime; // 시작시간
    private String endTime; // 종료시간
    private Boolean negotiation; // 근무 협의 여부
    private String workTerm; // 근무 가능 기간
}