package com.balybus.galaxy.helper.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CareRequestDTO {
    private List<String> preferredWorkTypes; // 희망하는 근무 종류 (최대 5개, 중복 가능)
    private List<String> preferredCareTypes; // 희망하는 돌봄 유형 (중복 가능)
    private String gender; // 성별 (남/여 선택 1)
    private String livingArrangement; // 동거인 여부 (선택 1)
    private List<String> mealAssistance; // 식사 보조 (중복 가능)
    private List<String> mobilityAssistance; // 이동 보조 (중복 가능)
    private List<String> dailyLifeAssistance; // 일상 생활 (중복 가능)
}
