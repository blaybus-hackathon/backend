package com.balybus.galaxy.centerManager.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class CmResponseDto {
    @Getter
    @Builder
    public static class GetOneManager {
        private Long centerSeq;         //소속 센터 구분자
        private String centerName;      //소속 센터 이름

        private Long cmSeq;             //관리자 구분자
        private String cmName;          //관리자 이름
        private String cmPosition;      //관리자 직책
        private Long imgSeq;            //이미지 구분자
        private String imgAddress;      //이미지 주소
    }

    @Getter
    @Builder
    public static class UpdateManager {
        private Long cmSeq;             //관리자 구분자
    }

    @Getter
    @Builder
    public static class GetOneCenter {
        private Long centerSeq;
        private String name;        // 센터 이름
        private String tel;         // 전화번호
        private Boolean carYn;      // 목욕차량 소유 여부
        private String address;     // 주소
        private String grade;       // 센터 등급
        private LocalDate openDate; // 운영 시작일
        private String introduce;   // 한줄소개
    }

    @Getter
    @Builder
    public static class UpdateCenter {
        private Long centerSeq;
    }

    @Getter
    public static class GetStatisticsDashboard {
        //일단, 공고 기준으로 데이터 집계 - 회의 후, 공고 기준이 아니라 매칭 자체 개수로 진행 될수도 있음.

        private final Long newCnt;              //신규 매칭 건수 : 해당 계정으로 관리하고 있는 완료되지 않은 매칭(공고) 건수 - init 만으로 구성된공고 개수
        private final Long totalCnt;            //전체 매칭 건수 : 해당 계정으로 관리하고 있는 매칭(공고) 건수

        private final Long stateWaitCnt;        //상태별 매칭 건수 - 대기 : 전체 매칭 건수에서 완료와 진행중 개수를 제외한 나머지 공고의 개수 - wait_count = total-match_fin_count-in_progress_count
        private final Long stateInProgressCnt;  //상태별 매칭 건수 - 진행중 : PERMIT_TUNE(2, "수락함(조율중)") 상태의 공고 개수 - 매칭 완료가 아닌 공고 중 수락함(조율 중) 상태의 건수가 하나라도 존재하는 경우, 진행중으로 여긴다.
        private final Long stateFinCnt;         //상태별 매칭 건수 - 완료 : MATCH_FIN(3, "매칭 완료") 상태의 공고 개수 - 매칭보호사 리스트 중 한 명이라도 완료가 되면, 해당 건은 매칭이 완료된 것으로 여긴다.

        private final BigDecimal permitRate;    //매칭 비율 - 수락률 : 매칭 리스트 전체에 대한 조율 매칭 완료 개수(수락)의 비율 - 수락/(수락+거절)
        private final BigDecimal rejectRate;    //매칭 비율 - 거절률 : 매칭 리스트 전체에 대한 거절 개수(거절)의 비율 - 거절/(수락+거절)

        public GetStatisticsDashboard(){
            this.newCnt = 0L;
            this.totalCnt = 0L;
            this.stateInProgressCnt = 0L;
            this.stateFinCnt = 0L;
            this.stateWaitCnt = 0L;
            this.permitRate = BigDecimal.ZERO;
            this.rejectRate = BigDecimal.ZERO;
        }

        public GetStatisticsDashboard(Object[] getStatistics){
            this.newCnt = (Long) getStatistics[0];
            this.totalCnt = (Long) getStatistics[1];
            this.stateInProgressCnt = (Long) getStatistics[2];
            this.stateFinCnt = (Long) getStatistics[3];
            this.stateWaitCnt = this.totalCnt - (this.stateInProgressCnt+this.stateFinCnt);
            if(this.totalCnt.equals(0L)){
                this.permitRate = null;
                this.rejectRate = null;
            } else {
                BigDecimal permitCnt = (BigDecimal) getStatistics[4];
                BigDecimal totalCnt = permitCnt.add((BigDecimal) getStatistics[5]);
                this.permitRate = permitCnt.divide(totalCnt,2, RoundingMode.HALF_EVEN);
                this.rejectRate = BigDecimal.ONE.subtract(permitRate);
            }
        }
    }


}
