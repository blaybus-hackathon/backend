package com.balybus.galaxy.domain.tblCare.service;

import com.balybus.galaxy.domain.tblCare.dto.CareBaseDto;
import com.balybus.galaxy.domain.tblCare.dto.CareChoiceListDto;
import com.balybus.galaxy.domain.tblCare.dto.CareRequestDto;
import com.balybus.galaxy.domain.tblCare.dto.CareResponseDto;

public interface TblCareService {
    CareResponseDto.GetAllCodeList getAllCodeList();    //전체 코드 조회
    CareResponseDto.GetServiceCodeList getServiceCodeList();    //어르신 필요 서비스 리스트 항목
    CareResponseDto.GetRequestCodeList getRequestCodeList(CareRequestDto.GetRequestCodeList req);   //요청 종류 리스트 항목
    CareChoiceListDto getCareChoiceList(CareBaseDto careDto, boolean needWelfare); // 선택한 돌봄 항목 항목별 pk 값 리스트 반환
}
