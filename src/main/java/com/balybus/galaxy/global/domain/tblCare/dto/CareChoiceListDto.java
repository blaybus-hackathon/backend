package com.balybus.galaxy.global.domain.tblCare.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CareChoiceListDto {
    private List<Long> workTypeList;         // 근무종류
    private List<Long> careLevelList;        // 장기요양등급
    private List<Long> dementiaSymptomList;  // 치매증상
    private List<Long> inmateStateList;      // 동거인 여부
    private List<Long> genderList;           // 성별

    private List<Long> serviceMealList;      // 식사보조
    private List<Long> serviceToiletList;    // 배변보조
    private List<Long> serviceMobilityList;  // 이동보조
    private List<Long> serviceDailyList;     // 일상생활

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> welfareList;          // 복리후생
}
