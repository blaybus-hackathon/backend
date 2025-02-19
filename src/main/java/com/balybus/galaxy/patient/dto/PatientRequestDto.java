package com.balybus.galaxy.patient.dto;

import com.balybus.galaxy.address.domain.TblAddressFirst;
import com.balybus.galaxy.address.domain.TblAddressSecond;
import com.balybus.galaxy.address.domain.TblAddressThird;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.patient.domain.tblPatient.TblPatient;
import com.balybus.galaxy.patient.domain.tblPatientTime.TblPatientTime;
import lombok.Data;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PatientRequestDto {

    @Data
    public static class SavePatientInfo{
        private Long afSeq;           // 광역시.도 구분자
        private Long asSeq;           // 시.군.구 구분자
        private Long atSeq;           // 읍.면.동 구분자
        private int careLevel;        // 장기요양등급(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private int inmateState;      // 동거인 여부(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private int workType;         // 근무종류(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private int gender;           // 남성:1 여성:2 : 선택된 항목의 careVal 값을 합산해주세요
        private int dementiaSymptom;  // 치매증상(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private int serviceMeal;      // 식사보조(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private int serviceToilet;    // 배변보조(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private int serviceMobility;  // 이동보조(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private int serviceDaily;     // 일상생활(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private String name;          // 어르신 이름
        private String birthDate;     // 생년월일
        private Double weight;        // 몸무게
        private String diseases;      // 보유 질병/질환
        private Boolean timeNegotiation;    // 돌봄 요일 시간 협의 여부
        private String requestContents;     // 기타 요청 사항

        List<savePatientTimeInfo> timeList; // 돌봄요일 리스트 : 선택한 요일에 대한 데이터만 리스트에 넣어서 전달.

        // entity 전환
        public TblPatient toEntity(
                TblCenterManager manager, TblAddressFirst tblAddressFirst,
                TblAddressSecond tblAddressSecond, TblAddressThird tblAddressThird){
            return TblPatient.builder()
                    .manager(manager)
                    .tblAddressFirst(tblAddressFirst)
                    .tblAddressSecond(tblAddressSecond)
                    .tblAddressThird(tblAddressThird)
                    .careLevel(this.careLevel)
                    .inmateState(this.inmateState)
                    .workType(this.workType)
                    .gender(this.gender)
                    .dementiaSymptom(this.dementiaSymptom)
                    .serviceMeal(this.serviceMeal)
                    .serviceToilet(this.serviceToilet)
                    .serviceMobility(this.serviceMobility)
                    .serviceDaily(this.serviceDaily)
                    .name(this.name)
                    .birthDate(this.birthDate)
                    .weight(this.weight)
                    .diseases(this.diseases)
                    .timeNegotiation(this.timeNegotiation)
                    .requestContents(this.requestContents)
                    .build();
        }
    }

    @Data
    public static class savePatientTimeInfo{
        private int ptDate;             // 요일(1:월 ~ 7:일)
        private String ptStartTime;  // 시작시간
        private String ptEndTime;    // 종료시간

        public TblPatientTime toEntity(TblPatient patient) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return TblPatientTime.builder()
                    .patient(patient)
                    .ptDate(ptDate)
                    .ptStartTime(LocalTime.parse(ptStartTime, formatter))
                    .ptEndTime(LocalTime.parse(ptEndTime, formatter))
                    .build();
        }
    }

    @Data
    public static class UpdatePatientInfo{
        private Long patientSeq;      //어르신 구분자
        private Long afSeq;           // 광역시.도 구분자
        private Long asSeq;           // 시.군.구 구분자
        private Long atSeq;           // 읍.면.동 구분자
        private int careLevel;        // 장기요양등급(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private int inmateState;      // 동거인 여부(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private int workType;         // 근무종류(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private int gender;           //남성:1 여성:2 : 선택된 항목의 careVal 값을 합산해주세요
        private int dementiaSymptom;  // 치매증상(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private int serviceMeal;      // 식사보조(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private int serviceToilet;    // 배변보조(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private int serviceMobility;  // 이동보조(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private int serviceDaily;     // 일상생활(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        private String name;          //어르신 이름
        private String birthDate;     // 생년월일
        private Double weight;        // 몸무게
        private String diseases;      // 보유 질병/질환
        private Boolean timeNegotiation;    // 돌봄 요일 시간 협의 여부
        private String requestContents;     // 기타 요청 사항

        List<savePatientTimeInfo> timeList; // 돌봄요일 리스트 : 선택한 요일에 대한 데이터만 리스트에 넣어서 전달.

    }

    @Data
    public static class RecruitHelper{

    }
}
