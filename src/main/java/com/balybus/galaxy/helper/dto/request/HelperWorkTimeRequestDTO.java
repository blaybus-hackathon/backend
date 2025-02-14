package com.balybus.galaxy.helper.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HelperWorkTimeRequestDTO {
    private Long helperId;
    private Integer date;
    private Float startTime;
    private Float endTime;

    public static boolean hasNullHelperWorkTimeRequestDTO(HelperWorkTimeRequestDTO helperWorkTimeRequestDTO) {
        return helperWorkTimeRequestDTO.getStartTime() == null
                || helperWorkTimeRequestDTO.getEndTime() == null
                || helperWorkTimeRequestDTO.getHelperId() == null
                || helperWorkTimeRequestDTO.getDate() == null;
    }

}