package com.balybus.galaxy.kakao.dto.request;

import com.balybus.galaxy.login.domain.type.RoleType;
import lombok.Data;
import org.hibernate.usertype.UserType;

@Data
public class KakaoRequest {
    private String code;
    private RoleType roleType;
}
