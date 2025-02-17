package com.balybus.galaxy.helper.dto.request;

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
    private List<String> experiences = new ArrayList<>();
    private List<String> ages = new ArrayList<>();
    private List<String> terms = new ArrayList<>();
    private List<Integer> genders = new ArrayList<>();
}
