package com.balybus.galaxy.global.domain.tblCenterManager.dto;

import lombok.Builder;
import lombok.Getter;

public class CenterManagerResponseDto {
    @Getter
    @Builder
    public static class SignUpManager{
        private Long cmSeq;     // 관리자 구분자 (CM_SEQ)
    }
}
