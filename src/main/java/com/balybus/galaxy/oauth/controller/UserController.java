package com.balybus.galaxy.oauth.controller;

import com.balybus.galaxy.oauth.dto.request.KakaoRequest;
import com.balybus.galaxy.oauth.dto.response.KakaoResponse;
import com.balybus.galaxy.oauth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "카카오 회원가입", description = "카카오 회원가입을 진행하기 위한 로직")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카카오 회원가입 성공", content = @Content(schema = @Schema(implementation = KakaoResponse.class)))
    })
    @PostMapping("/oauth/kakao-signup")
    public KakaoResponse getSignUp(@RequestBody KakaoRequest code, HttpServletRequest request, HttpServletResponse response) {

        return userService.kakaoLogin(code, request, response);
    }
}