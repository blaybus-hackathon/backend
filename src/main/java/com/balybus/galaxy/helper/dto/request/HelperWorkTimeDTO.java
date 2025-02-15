package com.balybus.galaxy.helper.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HelperWorkTimeDTO {
    private Integer day;   // 1:"monday", 2:"tuesday" 등
    private String startTime; // "09:00"
    private String endTime;   // "18:00"
}
