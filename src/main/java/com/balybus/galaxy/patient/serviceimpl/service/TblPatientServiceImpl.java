package com.balybus.galaxy.patient.serviceimpl.service;

import com.balybus.galaxy.patient.DTO.TblPatientResponseDTO;
import com.balybus.galaxy.patient.DTO.TblPatientSignUpDTO;
import com.balybus.galaxy.patient.DTO.TblPatientLoginDTO;
import com.balybus.galaxy.patient.domain.TblPatient;
import com.balybus.galaxy.patient.repository.TblPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TblPatientServiceImpl {

    private final TblPatientRepository patientRepository;

    public TblPatientResponseDTO registerPatient(TblPatientSignUpDTO signUpDTO) {
        // 어르신 이름 중복 체크
        if (patientRepository.findByName(signUpDTO.getName()).isPresent()) {
            throw new RuntimeException("이미 존재하는 어르신 이름입니다.");
        }

        // 어르신 엔티티 저장
        TblPatient patient = TblPatient.builder()
                .managerSeq(signUpDTO.getManagerSeq())
                .longTermCareGradeSeq(signUpDTO.getLongTermCareGradeSeq())
                .guardianInfoSeq(signUpDTO.getGuardianInfoSeq())
                .tblPatientFirst(signUpDTO.getTblPatientFirst())
                .tblPatientSecond(signUpDTO.getTblPatientSecond())
                .tblPatientThrid(signUpDTO.getTblPatientThrid())
                .name(signUpDTO.getName())
                .birthDate(signUpDTO.getBirthDate())
                .gender(signUpDTO.getGender())
                .weight(signUpDTO.getWeight())
                .diseases(signUpDTO.getDiseases())
                .build();

        TblPatient savedPatient = patientRepository.save(patient);

        // ResponseDTO 반환
        return TblPatientResponseDTO.builder()
                .id(savedPatient.getId())
                .managerSeq(savedPatient.getManagerSeq())
                .longTermCareGradeSeq(savedPatient.getLongTermCareGradeSeq())
                .guardianInfoSeq(savedPatient.getGuardianInfoSeq())
                .tblPatientFirst(savedPatient.getTblPatientFirst())
                .tblPatientSecond(savedPatient.getTblPatientSecond())
                .tblPatientThrid(savedPatient.getTblPatientThrid())
                .name(savedPatient.getName())
                .birthDate(savedPatient.getBirthDate())
                .gender(savedPatient.getGender())
                .weight(savedPatient.getWeight())
                .diseases(savedPatient.getDiseases())
                .createdAt(savedPatient.getCreatedAt())
                .updatedAt(savedPatient.getUpdatedAt())
                .build();
    }

    public TblPatientResponseDTO loginPatient(TblPatientLoginDTO loginDTO) {
        // 어르신 이름과 생년월일로 로그인 확인
        TblPatient patient = patientRepository.findByName(loginDTO.getName())
                .orElseThrow(() -> new RuntimeException("어르신을 찾을 수 없습니다."));

        if (!patient.getBirthDate().equals(loginDTO.getBirthDate())) {
            throw new RuntimeException("생년월일이 일치하지 않습니다.");
        }

        return TblPatientResponseDTO.builder()
                .id(patient.getId())
                .managerSeq(patient.getManagerSeq())
                .longTermCareGradeSeq(patient.getLongTermCareGradeSeq())
                .guardianInfoSeq(patient.getGuardianInfoSeq())
                .tblPatientFirst(patient.getTblPatientFirst())
                .tblPatientSecond(patient.getTblPatientSecond())
                .tblPatientThrid(patient.getTblPatientThrid())
                .name(patient.getName())
                .birthDate(patient.getBirthDate())
                .gender(patient.getGender())
                .weight(patient.getWeight())
                .diseases(patient.getDiseases())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
}
