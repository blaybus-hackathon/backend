package com.balybus.galaxy.global.domain.tblCare;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TblCareTopEnum {
    /* 1 depth */
    WORK_TYPE(1L, "근무종류"),
    WELFARE(9L,"복리후생"),
    CARE_LEVEL(20L,"장기요양등급"),
    DEMENTIA_SYMPTOM(28L,"치매증상"),
    INMATE_STATE(37L,"동거인 여부"),
    SERVICE(43L,"어르신 필요 서비스"),
    GENDER(66L,"성별"),

    /* 2 depth */
    SERVICE_MEAL(44L,"어르신 필요 서비스 - 식사보조"),
    SERVICE_TOILET(49L,"어르신 필요 서비스 - 배변보조"),
    SERVICE_MOBILITY(54L,"어르신 필요 서비스 - 이동보조"),
    SERVICE_DAILY(59L,"어르신 필요 서비스 - 일상생활"),
    ;

    private final Long careSeq;
    private final String title;
}
