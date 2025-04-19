package com.balybus.galaxy.patient.service;

import com.balybus.galaxy.patient.dto.PatientRequestDto;
import com.balybus.galaxy.patient.dto.PatientResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface PatientService {
    PatientResponseDto.SavePatientInfo savePatientInfo(String userEmail, PatientRequestDto.SavePatientInfo dto); // 어르신 등록
    PatientResponseDto.UpdatePatientInfo updatePatientInfo(String userEmail, PatientRequestDto.UpdatePatientInfo dto); // 어르신 정보 수정
    PatientResponseDto.GetOnePatientInfo getOnePatientInfo(String userEmail, Long patientSeq); // 어르신 정보 상세 조회
    PatientResponseDto.GetPatientList getPatientList(String userEmail,PatientRequestDto.GetPatientList dto); // 어르신 리스트 조회
    PatientResponseDto.RecruitHelper recruitHelper(UserDetails userDetails, PatientRequestDto.RecruitHelper dto); // 어르신 공고 등록
    PatientResponseDto.GetRecruitList getRecruitList(String userEmail,PatientRequestDto.GetRecruitList dto); // 어르신 공고 리스트 조회
    PatientResponseDto.GetRecruitList getRecruitPersonalList(String userEmail,PatientRequestDto.GetRecruitPersonalList dto); // 어르신 개인 공고 리스트 조회
    PatientResponseDto.GetOneRecruitPatientInfo getOneRecruitPatientInfo(String userEmail, Long patientLogSeq); // 어르신 정보 상세 조회
    PatientResponseDto.UpdateRecruitPatientInfo updateRecruitPatientInfo(String userEmail, PatientRequestDto.UpdateRecruitPatientInfo dto); // 어르신 정보 상세 조회
}
