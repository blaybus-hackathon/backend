package com.balybus.galaxy.patient.recruit.service;

import com.balybus.galaxy.patient.recruit.dto.RecruitRequestDto;
import com.balybus.galaxy.patient.recruit.dto.RecruitResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface RecruitService {
    RecruitResponseDto.RecruitHelper recruitHelper(UserDetails userDetails, RecruitRequestDto.RecruitHelper dto); // 어르신 공고 등록
    RecruitResponseDto.GetRecruitList getRecruitList(String userEmail, RecruitRequestDto.GetRecruitList dto); // 어르신 공고 리스트 조회
    RecruitResponseDto.GetRecruitList getRecruitPersonalList(String userEmail, RecruitRequestDto.GetRecruitPersonalList dto); // 어르신 개인 공고 리스트 조회
    RecruitResponseDto.GetOneRecruitPatientInfo getOneRecruitPatientInfo(String userEmail, Long patientLogSeq, boolean managerYn); // 어르신 정보 상세 조회
    RecruitResponseDto.UpdateRecruitPatientInfo updateRecruitPatientInfo(String userEmail, RecruitRequestDto.UpdateRecruitPatientInfo dto); // 어르신 정보 상세 조회
}
