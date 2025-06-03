package com.balybus.galaxy.careAssistant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelperSearchRequest {
    private List<String> experiences;
    private List<String> ages;
    private List<String> terms;
    private List<String> genders;
}
