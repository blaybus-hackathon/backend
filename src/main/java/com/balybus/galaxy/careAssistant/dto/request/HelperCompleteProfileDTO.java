package com.balybus.galaxy.careAssistant.dto.request;

import com.balybus.galaxy.login.classic.dto.request.HelperCertDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelperCompleteProfileDTO {
    // 프로필 정보
    private String introduce;
    private Boolean careExperience;
    
    // 자격증 정보
    private List<HelperCertDTO> certificates;
    private Boolean verifyQnet; // Q-net 검증 여부 (기본값: false)
    
    // 급여 정보
    private Integer wage;
    private Integer wageState;
    private Boolean wageNegotiation;
    
    // 근무 희망 지역
    private List<Long> addressFirstIds;
    private List<Long> addressSecondIds;
    private List<Long> addressThirdIds;
    
    // 근무 가능 시간
    private List<HelperWorkTimeDTO> workTimes;
    private Boolean negotiation; // 시간 협의 가능 여부
    private Integer workTerm; // 근무 기간
    
    // 경력 정보
    private HelperExperienceDTO experience;
    
    // 돌봄 서비스 정보
    private Integer careLevel;
    private Integer inmateState;
    private Integer workType;
    private Integer careGender;
    private Integer serviceMeal;
    private Integer serviceMobility;
    private Integer serviceDaily;
    
    // 기타 정보
    private Boolean carOwnYn;
    private Boolean eduYn;
}