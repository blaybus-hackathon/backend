package com.balybus.galaxy.careAssistant.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class HelperExperienceDTO {
    private String field;
    private LocalDateTime heStartDate;
    private LocalDateTime heEndDate;

    public static boolean hasNullHelperExperienceRequestDTO(HelperExperienceDTO helperExperienceDTO) {
        return helperExperienceDTO.getField() == null
                || helperExperienceDTO.getHeStartDate() == null
                || helperExperienceDTO.getHeEndDate() == null;
    }
}