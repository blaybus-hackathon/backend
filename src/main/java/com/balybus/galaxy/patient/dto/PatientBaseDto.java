package com.balybus.galaxy.patient.dto;

import com.balybus.galaxy.patient.domain.tblPatient.TblPatient;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLog;
import com.balybus.galaxy.patient.domain.tblPatientTime.TblPatientTime;
import com.balybus.galaxy.patient.domain.tblPatientTimeLog.TblPatientTimeLog;
import lombok.Data;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class PatientBaseDto {
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

    protected List<SavePatientTimeInfo> timeList; // 돌봄요일 리스트 : 선택한 요일에 대한 데이터만 리스트에 넣어서 전달.

    @Data
    public static class SavePatientTimeInfo{
        private int ptDate;             // 요일(1:월 ~ 7:일)
        private String ptStartTime;  // 시작시간
        private String ptEndTime;    // 종료시간

        public SavePatientTimeInfo(TblPatientTime entity){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            this.ptDate = entity.getPtDate();             // 요일(1:월 ~ 7:일)
            this.ptStartTime = entity.getPtStartTime().format(formatter);  // 시작시간
            this.ptEndTime = entity.getPtEndTime().format(formatter);    // 종료시간
        }

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

}
