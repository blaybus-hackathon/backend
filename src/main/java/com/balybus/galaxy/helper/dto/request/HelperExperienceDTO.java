package com.balybus.galaxy.helper.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class HelperExperienceDTO {
    private Long helperId;
    private String field;
    private LocalDateTime heStartDate;
    private LocalDateTime heEndDate;

    public static boolean hasNullHelperExperienceRequestDTO(HelperExperienceDTO helperExperienceDTO) {
        return helperExperienceDTO.getHelperId() == null
                || helperExperienceDTO.getField() == null
                || helperExperienceDTO.getHeStartDate() == null
                || helperExperienceDTO.getHeEndDate() == null;
    }
}