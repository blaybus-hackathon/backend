package com.balybus.galaxy.helper.dto.response;

import com.balybus.galaxy.helper.domain.TblHelperCert;
import com.balybus.galaxy.login.dto.request.HelperCertDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HelperResponse {
    private Long id;
    private String userEmail;
    private String name;
    private String phone;
    private String addressDetail;
    private List<HelperCertDTO> certificates;
    private boolean carOwnYn;
    private boolean eduYn;
    private Integer wage;
    private Integer wageState;
}
