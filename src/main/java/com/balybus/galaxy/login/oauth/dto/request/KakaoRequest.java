package com.balybus.galaxy.login.oauth.dto.request;

import com.balybus.galaxy.login.classic.domain.type.RoleType;
import lombok.Data;

@Data
public class KakaoRequest {
    private String code;
    private RoleType roleType;
}
