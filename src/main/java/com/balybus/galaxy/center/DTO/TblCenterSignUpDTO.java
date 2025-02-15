package com.balybus.galaxy.center.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TblCenterSignUpDTO {
    private Long imgSeq;
    private String centerName;
    private String centerTel;
    private Boolean centerCarYn;
    private String centerAddress;
    private String centerGrade;
    private LocalDateTime centerOpenDate;
    private String centerIntroduce;
}
