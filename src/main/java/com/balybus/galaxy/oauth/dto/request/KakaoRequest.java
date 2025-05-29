package com.balybus.galaxy.oauth.dto.request;

import com.balybus.galaxy.login.domain.type.RoleType;
import lombok.Data;

@Data
public class KakaoRequest {
    private String code;
    private RoleType roleType;
}
