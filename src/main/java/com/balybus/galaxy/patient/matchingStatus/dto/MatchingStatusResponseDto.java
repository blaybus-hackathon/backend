package com.balybus.galaxy.patient.matchingStatus.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    public static class MatchingStatusPatientInfoList {
        private List<MatchedFinPatientInfo> list;
    }

    // 요양보호사 기준 매칭 목록 조회 DTO
    @Getter
    @Builder
    public static class HelperMatchingList {
        private List<HelperMatchingInfo> list;
    }

    @Getter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MatchedFinPatientInfo extends MatchingPatientInfo {
        private List<MatchedHelperInfo> matchFinHelperInfoList;         //매칭 완료
        private List<MatchedHelperInfo> permitTuneHelperInfoList;       //채팅하기(조율중)
        private List<MatchedHelperInfo> matchRequestHelperInfoList;     //매칭 요청
        private List<MatchedHelperInfo> initHelperInfoList;             //매칭 요청 전
        private List<MatchedHelperInfo> rejectHelperInfoList;           //응답거절
        public MatchedFinPatientInfo(MatchingPatientInfo s
                , List<MatchedHelperInfo> matchFinHelperInfoList
                , List<MatchedHelperInfo> permitTuneHelperInfoList
                , List<MatchedHelperInfo> matchRequestHelperInfoList
                , List<MatchedHelperInfo> initHelperInfoList
                , List<MatchedHelperInfo> rejectHelperInfoList){
            super(s.patientSeq, s.patientLogSeq, s.name, s.birthDate, s.gender, s.workType, s.careLevel, s.addressFirst, s.addressSecond, s.addressThird);
            this.age = s.age;
            this.matchFinHelperInfoList = matchFinHelperInfoList;
            this.permitTuneHelperInfoList = permitTuneHelperInfoList;
            this.matchRequestHelperInfoList = matchRequestHelperInfoList;
            this.initHelperInfoList = initHelperInfoList;
            this.rejectHelperInfoList = rejectHelperInfoList;
        }
    }

    /**
     * 요양보호사 기준 매칭 정보 DTO
     */
    @Getter
    @NoArgsConstructor
    public static class HelperMatchingInfo {
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
        private String matchState; // 매칭 상태
        private String matchStateMsg; // 매칭 상태 메시지
        
        public HelperMatchingInfo(Long patientSeq, Long patientLogSeq,
                                  String name, String birthDate,
                                  String gender, String workType, String careLevel,
                                  String addressFirst, String addressSecond, String addressThird,
                                  String matchState, String matchStateMsg){
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
            this.matchState = matchState;
            this.matchStateMsg = matchStateMsg;
            this.age = calAge(birthDate, "yyyyMMdd"); //만나이 계산
        }
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

    @Getter
    @Builder
    public static class UpdatePatientMatchStatus{
        private int code;
        private String msg; //업데이트 결과 반환
    }
}
