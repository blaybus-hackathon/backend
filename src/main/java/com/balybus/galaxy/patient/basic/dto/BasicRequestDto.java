package com.balybus.galaxy.patient.basic.dto;

import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;
import com.balybus.galaxy.global.domain.tblAddressSecond.TblAddressSecond;
import com.balybus.galaxy.global.domain.tblAddressThird.TblAddressThird;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.domain.tblPatient.TblPatient;
import com.balybus.galaxy.patient.PatientBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class BasicRequestDto {
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
}
