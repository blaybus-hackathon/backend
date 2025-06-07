package com.balybus.galaxy.global.utils.address.dto.response;

import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;
import lombok.Getter;

@Getter
public class TblAddressFirstResponse {
    private Long id;
    private String name;

    public TblAddressFirstResponse(TblAddressFirst addressFirst) {
        this.id = addressFirst.getId();
        this.name = addressFirst.getName();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}
