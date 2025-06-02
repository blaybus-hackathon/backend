package com.balybus.galaxy.careAssistant.dto.request;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HelperWorkTimeRequestDTO {
    private List<HelperWorkTimeDTO> workTimes;
    private Boolean negotiation;
    private List<Integer> workTerm;
}