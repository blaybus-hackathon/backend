package com.balybus.galaxy.login.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class HelperCertDTO {
    private String certName;
    private String certNum;
    private Integer certDateIssue;
    private Integer certSerialNum;
}
