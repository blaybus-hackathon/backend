package com.balybus.galaxy.login.classic.controller;

import com.balybus.galaxy.login.classic.dto.request.RefreshTokenDTO;
import com.balybus.galaxy.global.exception.ErrorResponse;
import com.balybus.galaxy.login.classic.dto.response.AccessTokenResponse;
import com.balybus.galaxy.login.classic.dto.response.RefreshTokenResponse;
import com.balybus.galaxy.login.classic.service.login.LoginServiceImpl;
import com.balybus.galaxy.member.dto.request.MemberRequest;
import com.balybus.galaxy.member.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/sign")
public class LoginController {

    private final LoginServiceImpl loginService;

    /**
     * https 인증 확인용
     * @return String
     */
    @GetMapping("/healthcheck")
    public ResponseEntity<Void> healthcheck(){
        log.info("healthcheck :: SUCCESS");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 엑세스 토큰 재발급
     */
    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰을 사용하여 새 액세스 토큰을 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "새 액세스 토큰 발급 성공",
                    content = @Content(schema = @Schema(implementation = AccessTokenResponse.class))),
            @ApiResponse(responseCode = "2003", description = "리프레쉬 토큰을 확인해주세요.")
    })
    @GetMapping("/token/access-token")
    public ResponseEntity<AccessTokenResponse> renewAccessToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        String accessToken = loginService.renewAccessToken(refreshTokenDTO);
        log.info(accessToken);
        return ResponseEntity.ok().body(new AccessTokenResponse(accessToken));
    }

    /**
     * 리프레시 토큰 재발급
     */
    @Operation(summary = "리프레시 토큰 재발급", description = "새로운 리프레시 토큰을 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "새 리프레시 토큰 발급 성공",
                    content = @Content(schema = @Schema(implementation = RefreshTokenResponse.class))),
            @ApiResponse(responseCode = "2003", description = "리프레쉬 토큰을 확인해주세요. ")
    })
    @GetMapping("/token/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken() {
        String refreshToken = loginService.getRefreshToken();
        return ResponseEntity.ok().body(new RefreshTokenResponse(refreshToken));
    }

    /**
     * 로그인 API
     * @param dto MemberRequest.LoginDto
     * @return ResponseEntity
     */
    @PostMapping("/in")
    @Operation(summary = "로그인 API", description = "사용자가 아이디와 비밀번호로 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.SignInDto.class))),
            @ApiResponse(responseCode = "4002", description = "아이디/비밀번호 불일치",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "3000", description = "요양보호사 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "관리자 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> signIn(@RequestBody MemberRequest.SignInDto dto, HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok().body(loginService.signIn(dto, request, response));
    }

    /**
     * 이메일 찾기
     * @param email String
     * @return ResponseEntity
     */
    @GetMapping("/find-email/{email}")
    @Operation(summary = "로그인 이메일 찾기 API", description = "가입한 이메일을 찾습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자정의코드:가입된 이메일",
                    content = @Content(schema = @Schema(implementation = MemberResponse.FindEmail.class))),
            @ApiResponse(responseCode = "404", description = "사용자정의에러코드:미가입된 이메일",
                    content = @Content(schema = @Schema(implementation = MemberResponse.FindEmail.class)))
    })
    public ResponseEntity<?> findEmail(@PathVariable String email) {
        return ResponseEntity.ok().body(loginService.findEmail(email));
    }

    /**
     * 비밀번호 찾기(임시 비밀번호 발급)
     * @param email String
     * @return ResponseEntity
     */
    @GetMapping("/find-pwd/{email}")
    @Operation(summary = "로그인 비밀번호 찾기 API", description = "로그인 비밀번호를 찾습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자정의코드:가입된 이메일",
                    content = @Content(schema = @Schema(implementation = MemberResponse.FindEmail.class))),
            @ApiResponse(responseCode = "404", description = "사용자정의에러코드:미가입된 이메일",
                    content = @Content(schema = @Schema(implementation = MemberResponse.FindEmail.class)))
    })
    public ResponseEntity<?> findPwd(@PathVariable String email) {
        return ResponseEntity.ok().body(loginService.findPwd(email));
    }

}
