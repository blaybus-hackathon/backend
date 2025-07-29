package com.balybus.galaxy.careAssistant.dto.response;

import com.balybus.galaxy.careAssistant.domain.TblHelperWorkLocation;
import com.balybus.galaxy.careAssistant.domain.TblHelperWorkTime;
import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;
import com.balybus.galaxy.global.domain.tblAddressSecond.TblAddressSecond;
import com.balybus.galaxy.global.domain.tblAddressThird.TblAddressThird;
import com.balybus.galaxy.login.classic.dto.request.HelperCertDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HelperResponse {
    private Long id;
    private String userEmail;
    private String name;
    private String phone;
    private String addressDetail;
    private ImageDto img; // 프로필 사진
    private List<HelperWorkLocationDto> helperWorkLocation; // 선호 지역
    private List<HelperWorkTimeDto> helperWorkTime; // 근무 가능일정

    private int careLevel; // 장기 요양 등급
    private int inmateState; // 동거인 여부
    private int workType; // 근무 종류
    private int careGender; // 성별
    private int serviceMeal; // 식사 보조
    private int serviceMobility; // 이동 보조
    private int serviceDaily; // 일상 생활

    private List<HelperCertDTO> certificates; // 자격증 번호
    private boolean carOwnYn; // 요양보호사 차량 소유 여부
    private boolean eduYn; // 치매 교육 이수 여부
    private Integer wage; // 급여
    private Integer wageState; // 시급, 일급, 주급 구분
    private String introduce; // 자기소개
    private Boolean careExperience; // 간병 경력
    private Boolean wageNegotiation; // 급여 협의 가능 여부
}
