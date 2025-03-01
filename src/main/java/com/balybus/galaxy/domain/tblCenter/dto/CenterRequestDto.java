package com.balybus.galaxy.domain.tblCenter.dto;

import com.balybus.galaxy.domain.tblCenter.TblCenter;
import lombok.Data;

import java.time.LocalDate;

public class CenterRequestDto {
    @Data
    public static class RegisterCenter{
        private String name;        // 센터 이름
        private String tel;         // 전화번호
        private Boolean carYn;      // 목욕차량 소유 여부
        private String address;     // 주소
        private String grade;       // 센터 등급
        private LocalDate openDate; // 운영 시작일
        private String introduce;   // 한줄소개

        public TblCenter toEntity(String code) {
            return TblCenter.builder()
                    .centerCode(code)
                    .centerName(this.name)
                    .centerTel(this.tel)
                    .centerCarYn(this.carYn)
                    .centerAddress(this.address)
                    .centerGrade(this.grade)
                    .centerOpenDate(this.openDate)
                    .centerIntroduce(this.introduce)
                    .build();
        }
    }
}
