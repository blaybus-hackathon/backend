package com.balybus.galaxy.careAssistant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HelperWorkLocationDto {
    private Long afSeq; // 시.도 구분자
    private Long asSeq; // 시.군.구 구분자
    private Long atSeq; // 읍.면.동 구분자
}