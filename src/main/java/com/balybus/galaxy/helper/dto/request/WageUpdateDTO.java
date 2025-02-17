package com.balybus.galaxy.helper.dto.request;

import lombok.Getter;

@Getter
public class WageUpdateDTO {
    private Integer unit;
    private String wage;
    private Boolean wageNegotiation;
}
