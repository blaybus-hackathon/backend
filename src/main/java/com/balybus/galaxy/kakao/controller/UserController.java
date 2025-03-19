package com.balybus.galaxy.kakao.controller;

import com.balybus.galaxy.kakao.dto.request.KakaoRequest;
import com.balybus.galaxy.kakao.dto.request.KakaoUser;
import com.balybus.galaxy.kakao.dto.response.KakaoResponse;
import com.balybus.galaxy.kakao.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/oauth/kakao-signup")
    public KakaoResponse getSignUp(@RequestBody KakaoRequest code, HttpServletRequest request, HttpServletResponse response) {

        return userService.kakaoLogin(code, request, response);
    }
}