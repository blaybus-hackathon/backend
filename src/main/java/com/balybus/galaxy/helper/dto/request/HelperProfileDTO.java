package com.balybus.galaxy.helper.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelperProfileDTO {
    private String introduce;
    private Boolean careExperience;
    private String essentialCertNo;
    private String careCertNo;
    private String nurseCertNo;
    private String postPartumCertNo;
    @NotEmpty(message = "자격증은 최소 1개 이상 필수입니다.")
    private List<String> helperOtherCerts;


}
