package com.balybus.galaxy.patient.basic.service;

import com.balybus.galaxy.patient.dto.PatientRequestDto;
import com.balybus.galaxy.patient.dto.PatientResponseDto;

public interface BasicService {
    PatientResponseDto.SavePatientInfo savePatientInfo(String userEmail, PatientRequestDto.SavePatientInfo dto); // 어르신 등록
    PatientResponseDto.UpdatePatientInfo updatePatientInfo(String userEmail, PatientRequestDto.UpdatePatientInfo dto); // 어르신 정보 수정
    PatientResponseDto.GetOnePatientInfo getOnePatientInfo(String userEmail, Long patientSeq); // 어르신 정보 상세 조회
    PatientResponseDto.GetPatientList getPatientList(String userEmail,PatientRequestDto.GetPatientList dto); // 어르신 리스트 조회
}
