package com.balybus.galaxy.login.classic.dto.response;

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
public class HelperSignUpResponseDto {

    private Long helperSeq;
    List<HelperCertDTO> invalidCertList;
}
