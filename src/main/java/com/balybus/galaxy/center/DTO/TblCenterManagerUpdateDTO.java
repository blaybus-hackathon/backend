package com.balybus.galaxy.center.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TblCenterManagerUpdateDTO {
    private String centerName; // 센터명
    private String name; // 관리자 이름
    private String position; // 관리자 직책
}
