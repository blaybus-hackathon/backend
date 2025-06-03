package com.balybus.galaxy.careAssistant.dto.request;

import com.balybus.galaxy.login.classic.dto.request.HelperCertDTO;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "자격증은 최소 1개 이상 필수입니다.")
    private List<HelperCertDTO> certificates;


}
