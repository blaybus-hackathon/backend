package com.balybus.galaxy.login.classic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblHelperResponse {
    private String name;
    private String phone;
    private String addressDetail;

}
