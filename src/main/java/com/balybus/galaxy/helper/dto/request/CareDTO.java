package com.balybus.galaxy.helper.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareDTO {
    private Long topCareSeq;
    private String careName;
    private Integer careVal;
    private boolean careYn;
}
