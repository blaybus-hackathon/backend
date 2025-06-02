package com.balybus.galaxy.global.common.service;

import com.balybus.galaxy.global.domain.tblCare.dto.CareBaseDto;
import com.balybus.galaxy.global.domain.tblCare.dto.CareChoiceListDto;

public interface CommonService {
    CareChoiceListDto getCareChoiceList(CareBaseDto careDto, boolean needWelfare); // 선택한 돌봄 항목 항목별 pk 값 리스트 반환
}
