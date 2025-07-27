package com.balybus.galaxy.careAssistant.dto.request;

import com.balybus.galaxy.login.classic.dto.request.HelperCertDTO;
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
    // @NotEmpty 제거 - 자격증이 없어도 부분 업데이트 가능하도록
    private List<HelperCertDTO> certificates;
}
