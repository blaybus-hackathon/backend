package com.balybus.galaxy.helper.dto.response;

import com.balybus.galaxy.address.domain.TblAddressFirst;
import com.balybus.galaxy.address.domain.TblAddressThird;
import lombok.Getter;

@Getter
public class TblAddressFirstDTO {
    private Long id;
    private String name;

    public TblAddressFirstDTO(TblAddressFirst addressFirst) {
        this.id = addressFirst.getId();
        this.name = addressFirst.getName();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}
