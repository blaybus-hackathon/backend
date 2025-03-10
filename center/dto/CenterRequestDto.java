package com.balybus.galaxy.center.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CenterRequestDto {
    private Long id;             // 센터 ID
    private String issueCode;    // 발급코드
    private String name;         // 센터명
    private String address;      // 센터 주소
    private String description;  // 한줄소개
    private String contactNumber; // 연락처
    private String colorCode;    // 색상코드
    private LocalDate createdDate; // 생성일
    private Integer createdBy;   // 생성자 ID
    private LocalDateTime updatedDate; // 수정일
    private Integer updatedBy;   // 수정자 ID
}