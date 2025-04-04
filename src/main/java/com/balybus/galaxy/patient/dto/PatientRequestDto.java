package com.balybus.galaxy.patient.dto;

import com.balybus.galaxy.address.domain.TblAddressFirst;
import com.balybus.galaxy.address.domain.TblAddressSecond;
import com.balybus.galaxy.address.domain.TblAddressThird;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.patient.domain.tblPatient.TblPatient;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLog;
import com.balybus.galaxy.patient.dto.baseDto.PatientBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class PatientRequestDto {
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class SavePatientInfo extends PatientBaseDto {
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
                    .build();
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class UpdatePatientInfo extends PatientBaseDto {
        private Long patientSeq;      //어르신 구분자

    }

    @Data
    public static class GetPatientList {
        private Integer pageNo;
        private Integer pageSize;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class RecruitHelper extends UpdatePatientInfo {
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
                    .build();
        }
    }
}
