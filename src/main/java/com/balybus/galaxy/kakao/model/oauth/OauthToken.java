package com.balybus.galaxy.kakao.model.oauth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthToken {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;

    public String getAccessToken() {
        return access_token;
    }
}
