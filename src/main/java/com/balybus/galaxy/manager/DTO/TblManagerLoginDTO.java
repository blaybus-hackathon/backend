package com.balybus.galaxy.manager.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TblManagerLoginDTO {
    private Long userSeq;  // 로그인 구분자
    private String password;  // 비밀번호
}
