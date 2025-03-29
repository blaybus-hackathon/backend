package com.balybus.galaxy.domain.tblCare;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TblCareTopEnum {
    /* 1 depth */
    WORK_TYPE(1L, "근무종류", true),
    WELFARE(9L,"복리후생", false),
    CARE_LEVEL(20L,"장기요양등급", true),
    DEMENTIA_SYMPTOM(28L,"치매증상", true),
    INMATE_STATE(37L,"동거인 여부", true),
    SERVICE(43L,"어르신 필요 서비스", false),
    GENDER(66L,"성별", true),

    /* 2 depth */
    SERVICE_MEAL(44L,"어르신 필요 서비스 - 식사보조", true),
    SERVICE_TOILET(49L,"어르신 필요 서비스 - 배변보조", true),
    SERVICE_MOBILITY(54L,"어르신 필요 서비스 - 이동보조", true),
    SERVICE_DAILY(59L,"어르신 필요 서비스 - 일상생활", true),
    ;

    private final Long careSeq;
    private final String title;
    private final boolean patientEle;
}
