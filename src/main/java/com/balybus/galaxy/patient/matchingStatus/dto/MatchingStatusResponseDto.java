package com.balybus.galaxy.patient.matchingStatus.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MatchingStatusResponseDto {

    @Getter
    @Builder
    public static class MatchingWaitPatientInfoList{
        private List<MatchingWaitPatientInfo> matchingPatientInfoList;
    }

    @Getter
    @NoArgsConstructor
    public static class MatchingWaitPatientInfo extends MatchingPatientInfo {
        private List<MatchedHelperInfo> matchedHelperInfoList;
        public MatchingWaitPatientInfo(MatchingPatientInfo s, List<MatchedHelperInfo> matchedHelperInfoList){
            super(s.patientSeq, s.patientLogSeq, s.name, s.birthDate, s.gender, s.workType, s.careLevel, s.addressFirst, s.addressSecond, s.addressThird);
            this.age = s.age;
            this.matchedHelperInfoList = matchedHelperInfoList;
        }
    }

//    @Getter
//    @Builder
//    public static class MatchingPatientInfoList{
//        private List<MatchingPatientInfo> matchingPatientInfoList;
//    }
//
//    @Getter
//    @Builder
//    public static class MatchingPatientInfo {
//        private Long patientSeq; // 어르신 구분자
//        private Long patientLogSeq; // 어르신 공고 구분자
//        private String name; // 어르신 이름
//        private Integer gender; // 어르신 성별
//        private String birthDate; // 어르신 생년월일
//        private int workType; // 희망 근무 종류
//        private String tblAddressFirst; // 시.도
//        private String tblAddressSecond; // 시.군.구
//        private String tblAddressThird; // 읍.면.동
//        private List<MatchedHelperInfo> matchedHelperInfos;
//        private int careLevel; // 장기 요양 등급
//    }
//
//    @Getter
//    @Builder
//    public static class MatchedHelperInfo {
//        private Long helperSeq; // 매칭된 요양 보호사 구분자
//        private String name; // 매칭된 요양 보호사 이름
//        private Integer gender; // 매칭된 요양 보호사 성별
//        private String age; // 매칭된 요양 보호사 나이
//    }
//
//    @Getter
//    @Builder
//    public static class MatchedPatientInfoList{
//        private List<MatchingPatientInfo> matchedPatientInfoList;
//        private List<MatchingPatientInfo> matchingRejectedPatientInfoList;
//    }


    @Getter
    @Builder
    public static class UpdatePatientMatchStatus{
        private int code;
        private String msg; //업데이트 결과 반환
    }


    /* ===========================================================
     * COMMON
     * =========================================================== */
    /**
     * 만 나이 계산
     * @param birthDate String
     * @param pattern String
     * @return int:만 나이
     */
    private static int calAge(String birthDate, String pattern) {
        LocalDate currentDate = LocalDate.now();
        // 생일과 현재 날짜의 차이 계산
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        Period period = Period.between(LocalDate.parse(birthDate, formatter), currentDate);
        // 나이는 Period 객체의 연도를 반환
        return period.getYears();
    }

    /**
     * 어르신 공고 간소화 DTO
     */
    @Getter
    @NoArgsConstructor
    public static class MatchingPatientInfo {
        protected Long patientSeq; // 어르신 구분자
        protected Long patientLogSeq; // 어르신 공고 구분자
        protected String name; // 어르신 이름
        protected String birthDate; // 어르신 생년월일
        protected String gender; // 어르신 성별
        protected String workType; // 희망 근무 종류
        protected String careLevel; // 장기 요양 등급
        protected String addressFirst; // 시.도
        protected String addressSecond; // 시.군.구
        protected String addressThird; // 읍.면.동
        protected int age; // 어르신 만 나이
        public MatchingPatientInfo(Long patientSeq, Long patientLogSeq,
                                   String name, String birthDate,
                                   String gender, String workType, String careLevel,
                                   String addressFirst, String addressSecond, String addressThird){

            this.patientSeq = patientSeq;
            this.patientLogSeq = patientLogSeq;
            this.name = name;
            this.birthDate = birthDate;
            this.gender = gender;
            this.workType = workType;
            this.careLevel = careLevel;
            this.addressFirst = addressFirst;
            this.addressSecond = addressSecond;
            this.addressThird = addressThird;

            this.age = calAge(birthDate, "yyyyMMdd"); //만나이 계산
        }
    }

    /**
     * 요양보호사 간소화 DTO
     */
    @Getter
    @NoArgsConstructor
    public static class MatchedHelperInfo {
        private Long helperSeq; // 매칭된 요양 보호사 구분자
        private String name; // 매칭된 요양 보호사 이름
        private String gender; // 매칭된 요양 보호사 성별
        private int age; // 매칭된 요양 보호사 나이
        public MatchedHelperInfo(Long helperSeq, String name, String gender, String birthDate){
            this.helperSeq = helperSeq;
            this.name = name;
            this.gender = gender;
            this.age = calAge(birthDate, "yyyy:MM:dd");
        }
    }
}
