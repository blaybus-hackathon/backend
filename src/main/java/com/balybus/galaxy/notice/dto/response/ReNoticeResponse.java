package com.balybus.galaxy.notice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ReNoticeResponse {
    private Long id;
    private String name;
    private String birthDate;
    private String gender;
    private Long longTermCareGradeSeq;
    private String afSeq;
    private String asSeq;
    private String atSeq;

}
