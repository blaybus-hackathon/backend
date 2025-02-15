package com.balybus.galaxy.helper.dto.response;

import com.balybus.galaxy.address.domain.TblAddressSecond;
import lombok.Getter;

@Getter
public class TblAddressSecondDTO {
    private Long id;
    private String name;

    public TblAddressSecondDTO(TblAddressSecond entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
}
