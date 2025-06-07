package com.balybus.galaxy.careAssistant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelperSearchDTO {

    @Builder.Default
    private List<String> experiences = new ArrayList<>();

    @Builder.Default
    private List<String> ages = new ArrayList<>();

    @Builder.Default
    private List<String> terms = new ArrayList<>();

    @Builder.Default
    private List<Integer> genders = new ArrayList<>();
}
