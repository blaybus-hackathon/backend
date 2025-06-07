package com.balybus.galaxy.member.dto.response;

import com.balybus.galaxy.login.classic.domain.type.RoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberInfoResponse {
    private Long id;
    private String email;
    private String name;
    private RoleType userAuth;

}
