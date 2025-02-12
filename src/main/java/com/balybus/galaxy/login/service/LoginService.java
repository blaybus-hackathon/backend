package com.balybus.galaxy.login.service;

import com.balybus.galaxy.login.infrastructure.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final TokenProvider tokenProvider;

    public String renewAccessToken() {
        return tokenProvider.generateAccessToken("");
    }

    public String getRefreshToken() {
        return tokenProvider.refreshToken();
    }
}
