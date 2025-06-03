package com.balybus.galaxy.global.utils.address.dto.response;

import com.balybus.galaxy.global.domain.tblAddressSecond.TblAddressSecond;
import lombok.Getter;

@Getter
public class TblAddressSecondResponse {
    private Long id;
    private String name;

    public TblAddressSecondResponse(TblAddressSecond entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
}
