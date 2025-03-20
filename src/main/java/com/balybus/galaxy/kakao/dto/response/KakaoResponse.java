package com.balybus.galaxy.kakao.dto.response;

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
    private String description;
}