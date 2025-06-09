package com.balybus.galaxy.patient.matchingStatus.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MatchingStatusResponseDto {

    @Getter
    @Builder
    public static class MatchingPatientInfoList{
        private List<MatchingPatientInfo> matchingPatientInfoList;
    }

    @Getter
    @Builder
    public static class MatchingPatientInfo {
        private Long patientSeq; // 어르신 구분자
        private String name; // 어르신 이름
        private Integer gender; // 어르신 성별
        private String birthDate; // 어르신 생년월일
        private int workType; // 희망 근무 종류
        private String tblAddressFirst; // 시.도
        private String tblAddressSecond; // 시.군.구
        private String tblAddressThird; // 읍.면.동
        private List<MatchedHelperInfo> matchedHelperInfos;
        private int careLevel; // 장기 요양 등급
    }

    @Getter
    @Builder
    public static class MatchedHelperInfo {
        private Long helperSeq; // 매칭된 요양 보호사 구분자
        private String name; // 매칭된 요양 보호사 이름
        private Integer gender; // 매칭된 요양 보호사 성별
        private String age; // 매칭된 요양 보호사 나이
    }

    @Getter
    @Builder
    public static class MatchedPatientInfoList{
        private List<MatchingPatientInfo> matchedPatientInfoList;
        private List<MatchingPatientInfo> matchingRejectedPatientInfoList;
    }


    @Getter
    @Builder
    public static class UpdatePatientMatchStatus{
        private int code;
        private String msg; //업데이트 결과 반환
    }
}
