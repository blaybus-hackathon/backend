package com.balybus.galaxy.patient.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TblPatientLoginDTO {
    private String name;  // 어르신 이름 (로그인 ID)
    private String birthDate;  // 생년월일 (비밀번호 대체로 사용할 수 있음)
}
