package com.balybus.galaxy.patient.dto;

import com.balybus.galaxy.patient.domain.tblPatient.TblPatient;
import com.balybus.galaxy.patient.domain.tblPatientTime.TblPatientTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

public class PatientResponseDto {
    @Getter
    @SuperBuilder
    public static class basicDto {
        private Long patientSeq;        // 어르신 정보 구분자 값
        private String managerEmail;    // 담당자 이메일
    }

    @Getter
    @SuperBuilder
    public static class SavePatientInfo extends basicDto { }

    @Getter
    @SuperBuilder
    public static class UpdatePatientInfo extends basicDto { }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class GetOnePatientInfo extends PatientBaseDto {
        private List<Long> workTypeList;         // 근무종류
        private List<Long> careLevelList;        // 장기요양등급
        private List<Long> dementiaSymptomList;  // 치매증상
        private List<Long> inmateStateList;      // 동거인 여부
        private List<Long> genderList;           // 성별
        private List<Long> serviceMealList;      // 식사보조
        private List<Long> serviceToiletList;    // 배변보조
        private List<Long> serviceMobilityList;  // 이동보조
        private List<Long> serviceDailyList;     // 일상생활
        public GetOnePatientInfo(TblPatient patient, List<TblPatientTime> patientTimeList){
            this.afSeq = patient.getTblAddressFirst().getId();      // 광역시.도 구분자
            this.asSeq = patient.getTblAddressSecond().getId();     // 시.군.구 구분자
            this.atSeq = patient.getTblAddressThird().getId();      // 읍.면.동 구분자
            this.careLevel = patient.getCareLevel();                // 장기요양등급
            this.inmateState = patient.getInmateState();            // 동거인 여부
            this.workType = patient.getWorkType();                  // 근무종류
            this.gender = patient.getGender();                      // 남성:1 여성:2
            this.dementiaSymptom = patient.getDementiaSymptom();    // 치매증상
            this.serviceMeal = patient.getServiceMeal();            // 식사보조
            this.serviceToilet = patient.getServiceToilet();        // 배변보조
            this.serviceMobility = patient.getServiceMobility();    // 이동보조
            this.serviceDaily = patient.getServiceDaily();          // 일상생활
            this.name = patient.getName();                          // 어르신 이름
            this.birthDate = patient.getBirthDate();                // 생년월일
            this.weight = patient.getWeight();                      // 몸무게
            this.diseases = patient.getDiseases();                  // 보유 질병/질환
            this.timeNegotiation = patient.getTimeNegotiation();    // 돌봄 요일 시간 협의 여부

            List<SavePatientTimeInfo> timeList = new ArrayList<>();
            for(TblPatientTime patientTime: patientTimeList){
                timeList.add(new SavePatientTimeInfo(patientTime));
            }
            this.timeList = timeList; // 돌봄요일 리스트 : 선택한 요일에 대한 데이터만 리스트에 넣어서 전달.
        }

    }

    @Getter
    @Builder
    public static class RecruitHelper{
        private Long plSeq;         // 어르신 공고 구분자 값
        private Long patientSeq;    // 어르신 정보 구분자 값
        private String name;        // 이름
        private String birthYear;   // 생년월일 중 연도 반환
    }
}
