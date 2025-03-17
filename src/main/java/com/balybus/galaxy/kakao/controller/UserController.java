package com.balybus.galaxy.kakao.controller;

import com.balybus.galaxy.kakao.dto.request.KakaoRequest;
import com.balybus.galaxy.kakao.dto.request.KakaoUser;
import com.balybus.galaxy.kakao.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/oauth/token")
    public KakaoUser getLogin(@RequestBody KakaoRequest code) throws InterruptedException {

        return userService.kakaoLogin(code);
    }
}