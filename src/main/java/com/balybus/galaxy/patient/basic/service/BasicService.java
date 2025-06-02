package com.balybus.galaxy.patient.basic.service;

import com.balybus.galaxy.patient.basic.dto.BasicRequestDto;
import com.balybus.galaxy.patient.basic.dto.BasicResponseDto;

public interface BasicService {
    BasicResponseDto.SavePatientInfo savePatientInfo(String userEmail, BasicRequestDto.SavePatientInfo dto); // 어르신 등록
    BasicResponseDto.UpdatePatientInfo updatePatientInfo(String userEmail, BasicRequestDto.UpdatePatientInfo dto); // 어르신 정보 수정
    BasicResponseDto.GetOnePatientInfo getOnePatientInfo(String userEmail, Long patientSeq); // 어르신 정보 상세 조회
    BasicResponseDto.GetPatientList getPatientList(String userEmail,BasicRequestDto.GetPatientList dto); // 어르신 리스트 조회
}
