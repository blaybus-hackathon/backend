package com.balybus.galaxy.domain.tblCenter.dto;

import lombok.Builder;
import lombok.Getter;

public class CenterResponseDto {
    @Getter
    @Builder
    public static class RegisterCenter {
        private Long centerSeq;
    }
}
