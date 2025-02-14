package com.balybus.galaxy.helper.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HelperWorkLocationDTO {
    private Long HelperId;
    private String tblAddressFirst;
    private String tblAddressSecond;
    private String tblAddressThird;
}