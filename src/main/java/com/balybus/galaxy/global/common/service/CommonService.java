package com.balybus.galaxy.global.common.service;

import com.balybus.galaxy.global.domain.tblCare.dto.CareBaseDto;
import com.balybus.galaxy.global.domain.tblCare.dto.CareChoiceListDto;
import com.balybus.galaxy.global.domain.tblCare.dto.CareRequestDto;
import com.balybus.galaxy.global.domain.tblCare.dto.CareResponseDto;

public interface CommonService {
    CareChoiceListDto getCareChoiceList(CareBaseDto careDto, boolean needWelfare); // 선택한 돌봄 항목 항목별 pk 값 리스트 반환
}
