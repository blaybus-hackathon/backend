package com.balybus.galaxy.domain.tblCare;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TblCareTopEnum {
    /* 1 depth */
    WORK_TYPE(1L, "근무종류"),
    WELFARE(2L,"복리후생"),
    CARE_LEVEL(3L,"장기요양등급"),
    DEMENTIA_SYMPTOM(4L,"치매증상"),
    INMATE_STATE(5L,"동거인 여부"),
    SERVICE(6L,"어르신 필요 서비스"),

    /* 2 depth */
    SERVICE_MEAL(44L,"어르신 필요 서비스 - 식사보조"),
    SERVICE_TOILET(45L,"어르신 필요 서비스 - 배변보조"),
    SERVICE_MOBILITY(46L,"어르신 필요 서비스 - 이동보조"),
    SERVICE_DAILY(47L,"어르신 필요 서비스 - 일상생활"),
    ;

    private final Long careSeq;
    private final String title;
}
