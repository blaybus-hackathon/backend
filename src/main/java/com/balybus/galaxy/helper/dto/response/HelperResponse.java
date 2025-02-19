package com.balybus.galaxy.helper.dto.response;

import com.balybus.galaxy.member.domain.TblUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

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
    private String essentialCertNo;
    private String careCertNo;
    private String nurseCertNo;
    private String postPartumCertNo;
    private List<String> helperOtherCerts;
    private boolean carOwnYn;
    private boolean eduYn;
    private int wage;
    private int wageState;
}
