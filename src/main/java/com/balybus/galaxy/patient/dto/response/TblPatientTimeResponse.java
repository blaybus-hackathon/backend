package com.balybus.galaxy.patient.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder

public class TblPatientTimeResponse {
    private String pt_seq; //근무가능시건 구분자
    private Long id ; //어르신 구분자
    private int pt_date; //요일
    private LocalTime pt_start_time; // 시작시간
    private LocalTime pt_end_time; // 종료시간

}
