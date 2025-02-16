package com.balybus.galaxy.helper.dto.response;

import com.balybus.galaxy.address.domain.TblAddressSecond;
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
