package com.balybus.galaxy.login.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class AccessTokenResponse {

    private String accessToken;
}

