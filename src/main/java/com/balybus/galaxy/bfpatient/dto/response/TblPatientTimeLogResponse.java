package com.balybus.galaxy.bfpatient.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@Builder
public class TblPatientTimeLogResponse {
    private Long id;
    private int pt_date;
    private LocalTime pt_start_time;
    private LocalTime pt_end_time;
}
