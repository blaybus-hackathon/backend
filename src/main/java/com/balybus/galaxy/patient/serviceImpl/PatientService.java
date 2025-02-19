package com.balybus.galaxy.patient.serviceImpl;

import com.balybus.galaxy.patient.dto.PatientDto;
import com.balybus.galaxy.patient.dto.request.TblPatientDTO;
import com.balybus.galaxy.patient.dto.response.TblPatientResponse;

import java.util.List;

public interface PatientService {
    PatientDto registerPatient(PatientDto patientDto);
    PatientDto getPatientById(Long id);
    List<PatientDto> getAllPatients();

    TblPatientResponse registerPatient(TblPatientDTO patientDto);

    TblPatientResponse updatePatient(Long id, TblPatientDTO patientDto);
}
