package com.balybus.galaxy.global.domain.tblCare.service;

import com.balybus.galaxy.global.domain.tblCare.dto.CareRequestDto;
import com.balybus.galaxy.global.domain.tblCare.dto.CareResponseDto;

public interface TblCareService {
    CareResponseDto.GetAllCodeList getAllCodeList();    //전체 코드 조회
    CareResponseDto.GetServiceCodeList getServiceCodeList();    //어르신 필요 서비스 리스트 항목
    CareResponseDto.GetRequestCodeList getRequestCodeList(CareRequestDto.GetRequestCodeList req);   //요청 종류 리스트 항목
}
