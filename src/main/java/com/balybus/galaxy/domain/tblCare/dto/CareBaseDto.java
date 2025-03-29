package com.balybus.galaxy.domain.tblCare.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CareBaseDto {
    protected Integer careLevel;        // 장기요양등급
    protected Integer inmateState;      // 동거인여부
    protected Integer workType;         // 근무종류
    protected Integer gender;           // 남성:1 여성:2
    protected Integer dementiaSymptom;  // 치매증상
    protected Integer serviceMeal;      // 식사보조
    protected Integer serviceToilet;    // 배변보조
    protected Integer serviceMobility;  // 이동보조
    protected Integer serviceDaily;     // 일상생활
    protected Integer welfare;          // 복리후생
}
