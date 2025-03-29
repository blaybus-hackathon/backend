package com.balybus.galaxy.domain.tblCare.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class CareBaseDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Integer careLevel;        // 장기요양등급

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Integer inmateState;      // 동거인여부

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Integer workType;         // 근무종류

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Integer gender;           // 남성:1 여성:2

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Integer dementiaSymptom;  // 치매증상

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Integer serviceMeal;      // 식사보조

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Integer serviceToilet;    // 배변보조

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Integer serviceMobility;  // 이동보조

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Integer serviceDaily;     // 일상생활

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Integer welfare;          // 복리후생
}
