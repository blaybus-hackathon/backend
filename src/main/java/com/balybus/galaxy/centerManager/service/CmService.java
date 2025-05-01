package com.balybus.galaxy.centerManager.service;

import com.balybus.galaxy.centerManager.dto.CmRequestDto;
import com.balybus.galaxy.centerManager.dto.CmResponseDto;

public interface CmService {
    CmResponseDto.GetOneManager getOneManager(String userEmail); //센터 관리자 정보 조회
    CmResponseDto.UpdateManager updateManager(String userEmail, CmRequestDto.UpdateManager dto); //센터 관리자 정보 수정
    CmResponseDto.UpdateCenter updateCenter(String userEmail, CmRequestDto.UpdateCenter dto); //센터 정보 수정
}
