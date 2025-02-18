package com.balybus.galaxy.center.DTO;

import com.balybus.galaxy.address.domain.TblAddressFirst;
import com.balybus.galaxy.address.domain.TblAddressSecond;
import com.balybus.galaxy.address.domain.TblAddressThird;
import com.balybus.galaxy.patient.domain.TblPatient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TblPatientResponseDTO {

    List<PatientInfo> patientInfoList;


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientInfo {
        private String name;
        private int gender;
        private String age;
        private int workType;
        private String addressFirst;
        private String addressSecond;
        private String addressThree;
        private int careLevel;
    }
}