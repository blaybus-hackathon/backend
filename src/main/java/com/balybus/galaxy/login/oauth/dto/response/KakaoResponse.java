package com.balybus.galaxy.login.oauth.dto.response;

import com.balybus.galaxy.login.classic.domain.type.RoleType;
import com.balybus.galaxy.login.oauth.domain.type.CaseType;
import com.balybus.galaxy.member.domain.type.LoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class KakaoResponse {
    private String email;
    private String nickName;
    private LoginType loginType;
    private RoleType roleType;
    private String description;
    private CaseType caseType;
}