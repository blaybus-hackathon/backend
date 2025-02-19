package com.balybus.galaxy.patient.serviceImpl.service;

import com.balybus.galaxy.address.repository.TblAddressFirstRepository;
import com.balybus.galaxy.address.repository.TblAddressSecondRepository;
import com.balybus.galaxy.address.repository.TblAddressThirdRepository;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.patient.domain.TblPatient;
import com.balybus.galaxy.patient.dto.PatientDto;
import com.balybus.galaxy.patient.dto.request.TblPatientDTO;
import com.balybus.galaxy.patient.dto.response.TblPatientResponse;
import com.balybus.galaxy.patient.repository.TblPatientRepository;
import com.balybus.galaxy.patient.serviceImpl.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TblPatientServiceImpl implements PatientService {

    private final TblPatientRepository patientRepository;
    private final TblCenterManagerRepository managerRepository;
    private final TblAddressFirstRepository addressFirstRepository;
    private final TblAddressSecondRepository addressSecondRepository;
    private final TblAddressThirdRepository addressThirdRepository;

    /**
     *  환자 등록
     */
    @Override
    public TblPatientResponse registerPatient(TblPatientDTO patientDto) {
        TblCenterManager manager = managerRepository.findById(patientDto.getManagerSeq())
                .orElseThrow(() -> new RuntimeException("센터 관리자 정보를 찾을 수 없습니다."));

        TblPatient patient = TblPatient.builder()
                .manager(manager)
                .tblAddressFirst(addressFirstRepository.findById(Long.valueOf(patientDto.getTblPatientFirst()))
                        .orElseThrow(() -> new RuntimeException("시.도 구분자를 찾을 수 없습니다.")))
                .tblAddressSecond(addressSecondRepository.findById(Long.valueOf(patientDto.getTblPatientSecond()))
                        .orElseThrow(() -> new RuntimeException("시.군.구 구분자를 찾을 수 없습니다.")))
                .tblAddressThird(addressThirdRepository.findById(Long.valueOf(patientDto.getTblPatientThrid()))
                        .orElseThrow(() -> new RuntimeException("읍.면.동 구분자를 찾을 수 없습니다.")))
                .name(patientDto.getName())
                .birthDate(patientDto.getBirthDate())
                .gender(String.valueOf(Integer.parseInt(patientDto.getGender())))
                .weight(patientDto.getWeight())
                .diseases(patientDto.getDiseases())
                .build();

        TblPatient savedPatient = patientRepository.save(patient);
        return convertToResponse(savedPatient);
    }

    /**
     *  환자 정보 수정
     */
    @Override
    public TblPatientResponse updatePatient(Long id, TblPatientDTO patientDto) {
        TblPatient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("환자를 찾을 수 없습니다. ID: " + id));

        TblCenterManager manager = managerRepository.findById(patientDto.getManagerSeq())
                .orElseThrow(() -> new RuntimeException("센터 관리자 정보를 찾을 수 없습니다."));

        // 기존 환자 데이터를 새로운 값으로 변경
        TblPatient updatedPatient = TblPatient.builder()
                .id(patient.getId())
                .manager(manager)
                .tblAddressFirst(addressFirstRepository.findById(Long.valueOf(patientDto.getTblPatientFirst()))
                        .orElseThrow(() -> new RuntimeException("시.도 구분자를 찾을 수 없습니다.")))
                .tblAddressSecond(addressSecondRepository.findById(Long.valueOf(patientDto.getTblPatientSecond()))
                        .orElseThrow(() -> new RuntimeException("시.군.구 구분자를 찾을 수 없습니다.")))
                .tblAddressThird(addressThirdRepository.findById(Long.valueOf(patientDto.getTblPatientThrid()))
                        .orElseThrow(() -> new RuntimeException("읍.면.동 구분자를 찾을 수 없습니다.")))
                .name(patientDto.getName())
                .birthDate(patientDto.getBirthDate())
                .gender(String.valueOf(Integer.parseInt(patientDto.getGender())))
                .weight(patientDto.getWeight())
                .diseases(patientDto.getDiseases())
                .build();

        TblPatient savedPatient = patientRepository.save(updatedPatient);
        return convertToResponse(savedPatient);
    }

    /**
     * Response DTO로 변환
     */
    private TblPatientResponse convertToResponse(TblPatient patient) {
        return TblPatientResponse.builder()
                .id(patient.getId())
                .managerSeq(patient.getManager().getId())
                .tblPatientFirst(patient.getTblAddressFirst().toString())
                .tblPatientSecond(patient.getTblAddressSecond().toString())
                .tblPatientThrid(patient.getTblAddressThird().toString())
                .name(patient.getName())
                .birthDate(patient.getBirthDate())
                .gender(String.valueOf(patient.getGender()))
                .weight(patient.getWeight())
                .diseases(patient.getDiseases())
                .createdAt(patient.getCreateDatetime())
                .updatedAt(patient.getUpdateDatetime())
                .build();
    }



    @Override
    public PatientDto registerPatient(PatientDto patientDto) {
        return null;
    }

    @Override
    public PatientDto getPatientById(Long id) {
        return null;
    }

    @Override
    public List<PatientDto> getAllPatients() {
        return List.of();
    }
}
