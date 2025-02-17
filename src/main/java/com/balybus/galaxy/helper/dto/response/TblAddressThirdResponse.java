package com.balybus.galaxy.helper.dto.response;

import com.balybus.galaxy.address.domain.TblAddressThird;
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
