package com.balybus.galaxy.manager.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TblManagerResponseDTO {
    private Long userSeq;  // 유저 구분자
    private Long id;  // 관리자 구분자 (CM_SEQ)
    private String name;  // 직원명 (CM_NAME)
    private String position;  // 직책 (CM_POSITION)
    private String createdAt;  // 생성일시
    private String updatedAt;  // 수정일시
    private Long centerSeq;
}
