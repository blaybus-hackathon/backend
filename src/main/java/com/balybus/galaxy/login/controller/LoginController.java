package com.balybus.galaxy.login.controller;

import com.balybus.galaxy.login.dto.response.AccessTokenResponse;
import com.balybus.galaxy.login.service.LoginService;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/token")
    public ResponseEntity<AccessTokenResponse> renewAccessToken() {
        String accessToken = loginService.renewAccessToken();
        return ResponseEntity.ok().body(new AccessTokenResponse(accessToken));
    }
}
