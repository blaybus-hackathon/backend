package com.balybus.galaxy.careAssistant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelperCompleteProfileResponse {
    private boolean success;
    private String message;
    
    // 각 섹션별 처리 결과
    private SectionResult profileResult;
    private SectionResult certificateResult;
    private SectionResult wageResult;
    private SectionResult locationResult;
    private SectionResult workTimeResult;
    private SectionResult experienceResult;
    private SectionResult careServiceResult;
    
    // 자격증 검증 결과 (Q-net 검증한 경우)
    private Map<String, String> certificateVerificationResults;
    
    // 전체 업데이트된 프로필 정보
    private HelperResponse updatedProfile;
    
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SectionResult {
        private boolean updated;
        private boolean success;
        private String message;
        private String error;
    }
}