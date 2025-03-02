package com.balybus.galaxy.kakaoApi.controller;

import com.balybus.galaxy.kakaoApi.serviceImpl.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse response) {
        return ResponseEntity.ok(authService.oAuthLogin(accessCode, response));
    }
}
