package com.balybus.galaxy.domain.tblCare.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class CareResponseDto {
    @Getter
    @Builder
    public static class GetAllCodeList {
        private final List<TblCareDto> workTypeList;          // 근무종류
        private final List<TblCareDto> welfareList;           // 복리후생
        private final List<TblCareDto> careLevelList;         // 장기요양등급
        private final List<TblCareDto> dementiaSymptomList;   // 치매증상
        private final List<TblCareDto> inmateStateList;       // 동거인 여부
        private final List<TblCareDto> serviceMealList;       // 어르신 필요 서비스 - 식사보조
        private final List<TblCareDto> serviceToiletList;     // 어르신 필요 서비스 - 배변보조
        private final List<TblCareDto> serviceMobilityList;   // 어르신 필요 서비스 - 이동보조
        private final List<TblCareDto> serviceDailyList;      // 어르신 필요 서비스 - 일상생활
    }
    @Getter
    @Builder
    public static class GetServiceCodeList {
        private final List<TblCareDto> serviceMealList;       // 어르신 필요 서비스 - 식사보조
        private final List<TblCareDto> serviceToiletList;     // 어르신 필요 서비스 - 배변보조
        private final List<TblCareDto> serviceMobilityList;   // 어르신 필요 서비스 - 이동보조
        private final List<TblCareDto> serviceDailyList;      // 어르신 필요 서비스 - 일상생활
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GetRequestCodeList {
        private List<TblCareDto> workTypeList;          // 근무종류
        private List<TblCareDto> welfareList;           // 복리후생
        private List<TblCareDto> careLevelList;         // 장기요양등급
        private List<TblCareDto> dementiaSymptomList;   // 치매증상
        private List<TblCareDto> inmateStateList;       // 동거인 여부
        private List<TblCareDto> serviceList;           // 어르신 필요 서비스
        private List<TblCareDto> serviceMealList;       // 어르신 필요 서비스 - 식사보조
        private List<TblCareDto> serviceToiletList;     // 어르신 필요 서비스 - 배변보조
        private List<TblCareDto> serviceMobilityList;   // 어르신 필요 서비스 - 이동보조
        private List<TblCareDto> serviceDailyList;      // 어르신 필요 서비스 - 일상생활
    }
}
