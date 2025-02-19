package com.balybus.galaxy.patient.serviceImpl.service;

import com.balybus.galaxy.patient.dto.PatientDto;
import com.balybus.galaxy.patient.domain.TblPatient;
import com.balybus.galaxy.patient.dto.request.TblPatientDTO;
import com.balybus.galaxy.patient.dto.response.TblPatientResponse;
import com.balybus.galaxy.patient.repository.TblPatientRepository;
import com.balybus.galaxy.patient.serviceImpl.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final TblPatientRepository patientRepository;

    @Override
    public PatientDto registerPatient(PatientDto patientDto) {
        TblPatient patient = patientRepository.save(patientDto.toEntity());
        return PatientDto.fromEntity(patient);
    }

    @Override
    public PatientDto getPatientById(Long id) {
        TblPatient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("환자를 찾을 수 없습니다."));
        return PatientDto.fromEntity(patient);
    }

    @Override
    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(PatientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public TblPatientResponse registerPatient(TblPatientDTO patientDto) {
        return null;
    }

    @Override
    public TblPatientResponse updatePatient(Long id, TblPatientDTO patientDto) {
        return null;
    }
}
