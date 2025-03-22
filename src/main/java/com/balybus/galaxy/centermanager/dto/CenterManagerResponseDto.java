package com.balybus.galaxy.centermanager.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CenterManagerResponseDto {
    private Long id;  // 관리자 구분자 (CM_SEQ)
    private Long userSeq;  // 유저 구분자
    private Long centerSeq;
    private String name;  // 직원명 (CM_NAME)
    private String position;  // 직책 (CM_POSITION)
}