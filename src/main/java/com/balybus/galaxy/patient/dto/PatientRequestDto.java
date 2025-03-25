package com.balybus.galaxy.patient.dto;

import com.balybus.galaxy.address.domain.TblAddressFirst;
import com.balybus.galaxy.address.domain.TblAddressSecond;
import com.balybus.galaxy.address.domain.TblAddressThird;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.patient.domain.tblPatient.TblPatient;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLog;
import com.balybus.galaxy.patient.domain.tblPatientTime.TblPatientTime;
import com.balybus.galaxy.patient.domain.tblPatientTimeLog.TblPatientTimeLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PatientRequestDto {

    @Data
    public static class BaseRequestDto {
        protected Long afSeq;           // 광역시.도 구분자
        protected Long asSeq;           // 시.군.구 구분자
        protected Long atSeq;           // 읍.면.동 구분자
        protected int careLevel;        // 장기요양등급(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        protected int inmateState;      // 동거인 여부(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        protected int workType;         // 근무종류(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        protected int gender;           // 남성:1 여성:2 : 선택된 항목의 careVal 값을 합산해주세요
        protected int dementiaSymptom;  // 치매증상(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        protected int serviceMeal;      // 식사보조(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        protected int serviceToilet;    // 배변보조(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        protected int serviceMobility;  // 이동보조(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        protected int serviceDaily;     // 일상생활(TblCare) : 선택된 항목의 careVal 값을 합산해주세요
        protected String name;          // 어르신 이름
        protected String birthDate;     // 생년월일
        protected Double weight;        // 몸무게
        protected String diseases;      // 보유 질병/질환
        protected Boolean timeNegotiation;    // 돌봄 요일 시간 협의 여부
//        protected String requestContents;     // 기타 요청 사항

        protected List<savePatientTimeInfo> timeList; // 돌봄요일 리스트 : 선택한 요일에 대한 데이터만 리스트에 넣어서 전달.
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

        public TblPatientTimeLog toLogEntity(TblPatientLog patientLog) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return TblPatientTimeLog.builder()
                    .patientLog(patientLog)
                    .ptlDate(ptDate)
                    .ptlStartTime(LocalTime.parse(ptStartTime, formatter))
                    .ptlEndTime(LocalTime.parse(ptEndTime, formatter))
                    .build();
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class SavePatientInfo extends BaseRequestDto {
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
//                    .requestContents(this.requestContents)
                    .build();
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class UpdatePatientInfo extends BaseRequestDto {
        private Long patientSeq;      //어르신 구분자

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class RecruitHelper extends UpdatePatientInfo {
        private int welfare; // 복리후생(TblCare)
        private Boolean wageNegotiation; //급여 협의 여부
        private int wageState; // 1:시급, 2:일급, 3:주급 구분
        private int wage; // 급여

        // entity 전환
        public TblPatientLog toEntity(
                TblPatient patient, TblCenterManager manager,
                double timeWage, double dayWage, double weekWage,
                TblAddressFirst tblAddressFirst, TblAddressSecond tblAddressSecond, TblAddressThird tblAddressThird){

            return TblPatientLog.builder()
                    .welfare(this.welfare)
                    .wageNegotiation(this.wageNegotiation)
                    .wageState(this.wageState)
                    .wage(this.wage)
                    .timeWage(timeWage)
                    .dayWage(dayWage)
                    .weekWage(weekWage)
                    .patient(patient)
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
//                    .requestContents(this.requestContents)
                    .build();
        }
    }
}
