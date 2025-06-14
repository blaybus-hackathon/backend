package com.balybus.galaxy.patient.basic.dto;

import com.balybus.galaxy.global.domain.tblCare.dto.CareChoiceListDto;
import com.balybus.galaxy.global.domain.tblPatient.TblPatient;
import com.balybus.galaxy.global.domain.tblPatientTime.TblPatientTime;
import com.balybus.galaxy.patient.PatientBaseDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

public class BasicResponseDto {
    @Getter
    @SuperBuilder
    public static class BasicDto {
        private Long patientSeq;        // 어르신 정보 구분자 값
        private String managerEmail;    // 담당자 이메일
    }

    @Getter
    @SuperBuilder
    public static class SavePatientInfo extends BasicDto { }

    @Getter
    @SuperBuilder
    public static class UpdatePatientInfo extends BasicDto { }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class GetOnePatientInfo extends PatientBaseDto {
        private CareChoiceListDto careChoice;
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

        public void setCareBaseDtoNull(){
            this.careLevel = null;        // 장기요양등급
            this.inmateState = null;      // 동거인여부
            this.workType = null;         // 근무종류
            this.gender = null;           // 남성:1 여성:2
            this.dementiaSymptom = null;  // 치매증상
            this.serviceMeal = null;      // 식사보조
            this.serviceToilet = null;    // 배변보조
            this.serviceMobility = null;  // 이동보조
            this.serviceDaily = null;     // 일상생활
        }

    }

    @Getter
    @Builder
    public static class GetPatientList {
        private int totalPage;                  // 전페 페이지 개수
        private long totalEle;                  // 전체 리스트 개수
        private boolean hasNext;                // 다음 페이지 존재 여부
        private List<GetPatientListInfo> list;  // 리스트
    }

    @Getter
    @Builder
    public static class GetPatientListInfo {
        private Long patientSeq;        // 구분자
        private String imgAddress;      // 이미지 주소
        private String name;            // 이름
        private String genderStr;       // 성별
        private int age;                // 나이
        private String workType;  // 근무종류
        private String address;         // 주소지
        private String careLevelStr;    // 장기요양등급
    }
}
