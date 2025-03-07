package com.balybus.galaxy.kakao.controller;

import com.balybus.galaxy.kakao.model.User;
import com.balybus.galaxy.kakao.model.oauth.OauthToken;
import com.balybus.galaxy.kakao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3306", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/oauth/token")
    public OauthToken getLogin(@RequestParam("code") String code) throws InterruptedException {

        // 인가 코드를 통해 access_token 발급
        OauthToken oauthToken = userService.getAccessToken(code);

        // 발급 받은 accessToken으로 카카오 회원 정보 DB 저장
        User user = userService.saveUser(oauthToken.getAccessToken());

        return oauthToken;
    }
}