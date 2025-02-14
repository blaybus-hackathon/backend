package com.balybus.galaxy.login.controller;

import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.login.dto.request.SignUpDTO;
import com.balybus.galaxy.login.dto.response.AccessTokenResponse;
import com.balybus.galaxy.login.dto.response.RefreshTokenResponse;
import com.balybus.galaxy.login.serviceImpl.service.LoginServiceImpl;
import com.balybus.galaxy.member.dto.request.MemberRequest;
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

    @GetMapping("/access-token")
    public ResponseEntity<AccessTokenResponse> renewAccessToken() {
        String accessToken = loginService.renewAccessToken();
        return ResponseEntity.ok().body(new AccessTokenResponse(accessToken));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken() {
        String refreshToken = loginService.getRefreshToken();
        return ResponseEntity.ok().body(new RefreshTokenResponse(refreshToken));
    }

    /**
     * TBL_HELPER
     * @param signUpDTO
     * @return
     */
    @PutMapping("/sign-up")
    public ResponseEntity<SignUpDTO> signUp(@RequestBody SignUpDTO signUpDTO) {
        if(signUpDTO.hasNullDataBeforeSignUp(signUpDTO)) {
            throw new BadRequestException(SIGNUP_INFO_NULL);
        }
        loginService.signUp(signUpDTO);
        return ResponseEntity.ok(signUpDTO);
    }


    /**
     * 로그인
     * @param dto MemberRequest.LoginDto
     * @return ResponseEntity
     */
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody MemberRequest.SignInDto dto) {
        return ResponseEntity.ok().body(loginService.signIn(dto));
    }

}
