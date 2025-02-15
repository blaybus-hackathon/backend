package com.balybus.galaxy.manager.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TblManagerSignUpDTO {
    private Long userSeq;  // 유저 구분자
    private Long centerSeq;  // 센터 구분자
    private String position;  // 직책
    private String name;  // 직원명
    private String password;  // 비밀번호
}
