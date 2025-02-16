package com.balybus.galaxy.helper.dto.response;

import lombok.Getter;

@Getter
public class TblAddressFirstResponse {
    private Long id;
    private String name;

    public TblAddressFirstResponse(com.balybus.galaxy.address.domain.TblAddressFirst addressFirst) {
        this.id = addressFirst.getId();
        this.name = addressFirst.getName();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}
