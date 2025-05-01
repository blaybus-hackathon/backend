package com.balybus.galaxy.centerManager.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class CmResponseDto {
    @Getter
    @Builder
    public static class GetOneManager {
        private Long centerSeq;         //소속 센터 구분자
        private String centerName;      //소속 센터 이름

        private Long cmSeq;             //관리자 구분자
        private String cmName;          //관리자 이름
        private String cmPosition;      //관리자 직책
        private Long imgSeq;            //이미지 구분자
        private String imgAddress;      //이미지 주소
    }

    @Getter
    @Builder
    public static class UpdateManager {
        private Long cmSeq;             //관리자 구분자
    }

    @Getter
    @Builder
    public static class GetOneCenter {
        private Long centerSeq;
        private String name;        // 센터 이름
        private String tel;         // 전화번호
        private Boolean carYn;      // 목욕차량 소유 여부
        private String address;     // 주소
        private String grade;       // 센터 등급
        private LocalDate openDate; // 운영 시작일
        private String introduce;   // 한줄소개
    }

    @Getter
    @Builder
    public static class UpdateCenter {
        private Long centerSeq;
    }
}
