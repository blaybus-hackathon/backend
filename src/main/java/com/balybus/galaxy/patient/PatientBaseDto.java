package com.balybus.galaxy.patient;

import com.balybus.galaxy.global.domain.tblCare.dto.CareBaseDto;
import com.balybus.galaxy.global.domain.tblPatient.TblPatient;
import com.balybus.galaxy.global.domain.tblPatientLog.TblPatientLog;
import com.balybus.galaxy.global.domain.tblPatientTime.TblPatientTime;
import com.balybus.galaxy.global.domain.tblPatientTimeLog.TblPatientTimeLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PatientBaseDto extends CareBaseDto {
    protected Long afSeq;           // 광역시.도 구분자
    protected Long asSeq;           // 시.군.구 구분자
    protected Long atSeq;           // 읍.면.동 구분자
    protected String name;          // 어르신 이름
    protected String birthDate;     // 생년월일
    protected Double weight;        // 몸무게
    protected String diseases;      // 보유 질병/질환
    protected Boolean timeNegotiation;    // 돌봄 요일 시간 협의 여부

    protected List<SavePatientTimeInfo> timeList; // 돌봄요일 리스트 : 선택한 요일에 대한 데이터만 리스트에 넣어서 전달.

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
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
        public SavePatientTimeInfo(TblPatientTimeLog entity){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            this.ptDate = entity.getPtlDate();             // 요일(1:월 ~ 7:일)
            this.ptStartTime = entity.getPtlStartTime().format(formatter);  // 시작시간
            this.ptEndTime = entity.getPtlEndTime().format(formatter);    // 종료시간
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
