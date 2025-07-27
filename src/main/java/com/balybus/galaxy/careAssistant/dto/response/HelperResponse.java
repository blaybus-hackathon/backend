package com.balybus.galaxy.careAssistant.dto.response;

import com.balybus.galaxy.login.classic.dto.request.HelperCertDTO;
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
    private String introduce;
    private Boolean careExperience;
}
