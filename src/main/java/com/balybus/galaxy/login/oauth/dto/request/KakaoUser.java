package com.balybus.galaxy.login.oauth.dto.request;

import lombok.Data;

@Data
public class KakaoUser {
    private String id;
    private String email;
    private String nickname;
    private String profileImage;
}