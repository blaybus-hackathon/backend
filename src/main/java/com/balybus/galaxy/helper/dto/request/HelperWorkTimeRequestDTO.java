package com.balybus.galaxy.helper.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HelperWorkTimeRequestDTO {
    private Long helperId;
    private List<HelperWorkTimeDTO> workTimes;
}