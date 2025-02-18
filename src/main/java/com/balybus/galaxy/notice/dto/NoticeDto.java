package com.balybus.galaxy.notice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeDto {
    private Long id;
    private String name;
    private String birthDate;
    private String gender;
    private Long longTermCareGradeSeq;
    private String afSeq;
    private String asSeq;
    private String atSeq;
}
