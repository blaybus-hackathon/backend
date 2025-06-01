package com.balybus.galaxy.global.domain.tblCare.dto;

import com.balybus.galaxy.global.domain.tblCare.TblCare;
import lombok.Getter;

@Getter
public class TblCareDto {
    private final Long id;            //어르신 케어항목 구분자
    private final Long topCareId;       //상위 구분자
    private final String careName;    //어르신 케어항목
    private final Integer careVal;    //비트 전환시 해당되는 값(1/2/4/8/16... 으로 할당됨)

    public TblCareDto(TblCare entity){
        this.id = entity.getId();
        this.topCareId = entity.getCare() == null ? null : entity.getCare().getId();
        this.careName = entity.getCareName();
        this.careVal = entity.getCareVal();
    }
}
