package com.balybus.galaxy.patient.serviceImpl;

import com.balybus.galaxy.patient.dto.PatientDto;

import java.util.List;

public interface PatientService {
    PatientDto registerPatient(PatientDto patientDto);
    PatientDto getPatientById(Long id);
    List<PatientDto> getAllPatients();
}
