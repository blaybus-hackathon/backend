package com.balybus.galaxy.patient.recruit.dto;

import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;
import com.balybus.galaxy.global.domain.tblAddressSecond.TblAddressSecond;
import com.balybus.galaxy.global.domain.tblAddressThird.TblAddressThird;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.patient.basic.dto.BasicRequestDto;
import com.balybus.galaxy.global.domain.tblPatient.TblPatient;
import com.balybus.galaxy.global.domain.tblPatientLog.TblPatientLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class RecruitRequestDto {
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class RecruitHelper extends BasicRequestDto.UpdatePatientInfo {
        private Boolean linkingYn;          //어르신 기본정보로 업데이트 할 것인지 여부
        private Boolean wageNegotiation;    //급여 협의 여부
        private int wageState;              // 1:시급, 2:일급, 3:주급 구분
        private int wage;                   // 급여
        private String requestContents;     // 기타 요청 사항

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
                    .requestContents(this.requestContents)
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
                    .build();
        }
    }

    @Data
    public static class GetRecruitList {
        private Integer pageNo;
        private Integer pageSize;
    }

    @Data
    public static class GetRecruitPersonalList {
        private Integer pageNo;
        private Integer pageSize;
        private Long patientSeq;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class UpdateRecruitPatientInfo extends RecruitHelper {
        private boolean reMatchYn;      //매칭 추천 리스트 다시 발급 필요 여부
        private Long patientLogSeq;     //어르신 공고 구분자
    }
}
