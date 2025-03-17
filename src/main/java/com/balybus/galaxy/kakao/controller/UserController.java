package com.balybus.galaxy.kakao.controller;

import com.balybus.galaxy.kakao.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/oauth/token")
    public String getLogin(@RequestParam("code") String code) throws InterruptedException {

        return userService.kakaoLogin(code);
    }
}