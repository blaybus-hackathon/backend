package com.balybus.galaxy.patient.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TblPatientSignUpDTO {
    private Long managerSeq;  // 관리자 구분자
    private Long longTermCareGradeSeq;  // 어르신 장기요양등급 구분자
    private Long guardianInfoSeq;  // 보호자 정보 구분자
    private String tblPatientFirst;  // 시도 구분자 (주소)
    private String tblPatientSecond;  // 시군구 구분자 (주소)
    private String tblPatientThrid;  // 읍면동 구분자 (주소)
    private String name;  // 어르신 이름
    private String birthDate;  // 생년월일
    private String gender;  // 성별
    private Double weight;  // 몸무게
    private String diseases;  // 보유 질병/질환
}
