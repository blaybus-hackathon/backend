package com.balybus.galaxy.helper.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HelperExperienceResponse {
    private String helperName;
    private String filed;
}
