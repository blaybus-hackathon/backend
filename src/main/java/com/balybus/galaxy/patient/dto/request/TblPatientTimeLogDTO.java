package com.balybus.galaxy.patient.dto.request;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class TblPatientTimeLogDTO {

    private Long id;
    private int pt_date;
    private LocalTime pt_start_time;
    private LocalTime pt_end_time;
}
