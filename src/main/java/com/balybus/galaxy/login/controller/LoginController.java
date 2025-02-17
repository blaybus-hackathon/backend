package com.balybus.galaxy.login.controller;

import com.balybus.galaxy.domain.tblCenterManager.dto.CenterManagerRequestDto;
import com.balybus.galaxy.domain.tblCenterManager.dto.CenterManagerResponseDto;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.utils.mail.dto.MailRequestDto;
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
@RequestMapping("/api/sign")
public class LoginController {

    private final LoginServiceImpl loginService;

    /**
     * 엑세스 토큰 재발급
     * @param refreshTokenDTO
     * @return 액세스 토큰
     */
    @GetMapping("/token/access-token")
    public ResponseEntity<AccessTokenResponse> renewAccessToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        String accessToken = loginService.renewAccessToken(refreshTokenDTO);
        log.info(accessToken);
        return ResponseEntity.ok().body(new AccessTokenResponse(accessToken));
    }

    /**
     * 레프레시 토큰 재발급
     * @return 리프레시 토큰
     */
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
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> signIn(@RequestBody MemberRequest.SignInDto dto) {
        return ResponseEntity.ok().body(loginService.signIn(dto));
    }

    /**
     * 회원가입시 이메일 인증 API
     * @param dto MailRequestDto.AuthenticationMail
     * @return ResponseEntity<?>
     */
    @PostMapping("/authentication-mail")
    @Operation(summary = "회원가입시 이메일 인증 API",
            description = "주체(요양보호사, 관리자, 어르신)의 프로필 이미지를 업로드하고 서버에 저장하는 기능을 제공합니다. " +
                    "이미지 파일은 multipart/form-data로 전송해야 하며, 성공적으로 업로드되면 이미지 구분자(imgSeq)가 반환됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 이미지 변경 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.SignInDto.class))),
            @ApiResponse(responseCode = "4000", description = "사용자정의에러코드:등록된 이메일이 존재합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> authenticationMail(@RequestBody MailRequestDto.AuthenticationMail dto) {
        return ResponseEntity.ok(loginService.authenticationMail(dto));
    }


    /**
     * 요양 보호사 회원 가입
     * @param signUpDTO SignUpDTO
     * @return ResponseEntity<TblHelperResponse>
     */
    @PutMapping("/up/helper")
    public ResponseEntity<TblHelperResponse> signUp(@RequestBody SignUpDTO signUpDTO) {
        if(signUpDTO.hasNullDataBeforeSignUp(signUpDTO)) {
            throw new BadRequestException(SIGNUP_INFO_NULL);
        }
        TblHelperResponse helperResponse = loginService.signUp(signUpDTO);
        return ResponseEntity.ok(helperResponse);
    }


    /**
     * 관리자(센터직원) 회원 가입
     * @param signUpDTO CenterManagerRequestDto
     * @return ResponseEntity<CenterManagerResponseDto>
     */
    @PostMapping("/up/manager")
    public ResponseEntity<CenterManagerResponseDto> managerInfo(@RequestBody CenterManagerRequestDto signUpDTO) {
        return ResponseEntity.ok(loginService.registerManager(signUpDTO));
    }

}
