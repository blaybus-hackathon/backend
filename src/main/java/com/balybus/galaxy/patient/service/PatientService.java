package com.balybus.galaxy.patient.service;

import com.balybus.galaxy.patient.dto.PatientRequestDto;
import com.balybus.galaxy.patient.dto.PatientResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface PatientService {
    PatientResponseDto.SavePatientInfo savePatientInfo(String userEmail, PatientRequestDto.SavePatientInfo dto); // 어르신 등록
    PatientResponseDto.UpdatePatientInfo updatePatientInfo(UserDetails userDetails, PatientRequestDto.UpdatePatientInfo dto); // 어르신 정보 수정
    PatientResponseDto.RecruitHelper recruitHelper(UserDetails userDetails, PatientRequestDto.RecruitHelper dto); // 어르신 공고 등록
}
