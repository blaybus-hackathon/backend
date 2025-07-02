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
    public static class MatchingPatientInfoList2{
        private List<MatchingPatientInfo2> matchingPatientInfoList;
    }

    @Getter
    @NoArgsConstructor
    public static class MatchingPatientInfo2 {
        private Long patientSeq; // 어르신 구분자
        private Long patientLogSeq; // 어르신 공고 구분자
        private String name; // 어르신 이름
        private String birthDate; // 어르신 생년월일
        private String gender; // 어르신 성별
        private String workType; // 희망 근무 종류
        private String careLevel; // 장기 요양 등급
        private String addressFirst; // 시.도
        private String addressSecond; // 시.군.구
        private String addressThird; // 읍.면.동
        private int age; // 어르신 만 나이
        private List<MatchedHelperInfo2> matchedHelperInfoList;

        public MatchingPatientInfo2(Long patientSeq, Long patientLogSeq,
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
        public void matchedHelperInfoList(List<MatchedHelperInfo2> data){
            this.matchedHelperInfoList = data;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class MatchedHelperInfo2 {
        private Long helperSeq; // 매칭된 요양 보호사 구분자
        private String name; // 매칭된 요양 보호사 이름
        private String gender; // 매칭된 요양 보호사 성별
        private int age; // 매칭된 요양 보호사 나이

        public MatchedHelperInfo2(Long helperSeq, String name, String gender, String birthDate){
            this.helperSeq = helperSeq;
            this.name = name;
            this.gender = gender;
            this.age = calAge(birthDate, "yyyy:MM:dd");
        }
    }

    //만 나이 계산
    private static int calAge(String birthDate, String pattern) {
        LocalDate currentDate = LocalDate.now();
        // 생일과 현재 날짜의 차이 계산
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        Period period = Period.between(LocalDate.parse(birthDate, formatter), currentDate);
        // 나이는 Period 객체의 연도를 반환
        return period.getYears();
    }









    @Getter
    @Builder
    public static class MatchingPatientInfoList{
        private List<MatchingPatientInfo> matchingPatientInfoList;
    }

    @Getter
    @Builder
    public static class MatchingPatientInfo {
        private Long patientSeq; // 어르신 구분자
        private Long patientLogSeq; // 어르신 공고 구분자
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
