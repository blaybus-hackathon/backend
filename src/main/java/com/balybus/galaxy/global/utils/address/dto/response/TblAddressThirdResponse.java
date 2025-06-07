package com.balybus.galaxy.global.utils.address.dto.response;

import com.balybus.galaxy.global.domain.tblAddressThird.TblAddressThird;
import lombok.Getter;

@Getter
public class TblAddressThirdResponse {
    private Long id;
    private String name;

    public TblAddressThirdResponse(TblAddressThird entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
}
