package com.balybus.galaxy.global.domain.tblCenterManager.dto;

import lombok.*;

public class CenterManagerRequestDto {
    @Data
    public static class SignUpManager{
        private Long centerSeq;     // 센터 구분자
        private String name;        // 직원명
        private String position;    // 직책
        private String email;       // 아이디
        private String password;    // 비밀번호
    }
}
