package com.balybus.galaxy.login.controller;

import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.login.dto.request.RefreshTokenDTO;
import com.balybus.galaxy.global.exception.ErrorResponse;
import com.balybus.galaxy.login.dto.request.SignUpDTO;
import com.balybus.galaxy.login.dto.response.AccessTokenResponse;
import com.balybus.galaxy.login.dto.response.RefreshTokenResponse;
import com.balybus.galaxy.login.dto.response.TblHelperResponse;
import com.balybus.galaxy.login.serviceImpl.service.LoginServiceImpl;
import com.balybus.galaxy.member.dto.request.MemberRequest;
import com.balybus.galaxy.member.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.balybus.galaxy.global.exception.ExceptionCode.SIGNUP_INFO_NULL;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final LoginServiceImpl loginService;

    @GetMapping("/token/access-token")
    public ResponseEntity<AccessTokenResponse> renewAccessToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        String accessToken = loginService.renewAccessToken(refreshTokenDTO);
        return ResponseEntity.ok().body(new AccessTokenResponse(accessToken));
    }

    @GetMapping("/token/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken() {
        String refreshToken = loginService.getRefreshToken();
        return ResponseEntity.ok().body(new RefreshTokenResponse(refreshToken));
    }

    /**
     * 요양 보호사 회원 가입
     * @param signUpDTO SignUpDTO
     * @return ResponseEntity<TblHelperResponse>
     */
    @PutMapping("/sign-up")
    public ResponseEntity<TblHelperResponse> signUp(@RequestBody SignUpDTO signUpDTO) {
        if(signUpDTO.hasNullDataBeforeSignUp(signUpDTO)) {
            throw new BadRequestException(SIGNUP_INFO_NULL);
        }
        TblHelperResponse helperResponse = loginService.signUp(signUpDTO);
        return ResponseEntity.ok(helperResponse);
    }

    /**
     * 로그인
     * @param dto MemberRequest.LoginDto
     * @return ResponseEntity
     */
    @PostMapping("/sign-in")
    @Operation(summary = "로그인 API", description = "사용자가 아이디와 비밀번호로 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.SignInDto.class))),
            @ApiResponse(responseCode = "4002", description = "아이디/비밀번호 불일치",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> signIn(@RequestBody MemberRequest.SignInDto dto) {
        return ResponseEntity.ok().body(loginService.signIn(dto));
    }

}
