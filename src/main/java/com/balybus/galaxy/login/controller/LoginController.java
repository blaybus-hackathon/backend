package com.balybus.galaxy.login.controller;

import com.balybus.galaxy.domain.tblCenter.dto.CenterRequestDto;
import com.balybus.galaxy.domain.tblCenter.dto.CenterResponseDto;
import com.balybus.galaxy.domain.tblCenterManager.dto.CenterManagerRequestDto;
import com.balybus.galaxy.domain.tblCenterManager.dto.CenterManagerResponseDto;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.utils.mail.dto.MailRequestDto;
import com.balybus.galaxy.global.utils.mail.dto.MailResponseDto;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<?> signIn(@RequestBody MemberRequest.SignInDto dto, HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok().body(loginService.signIn(dto, request, response));
    }

    /**
     * 회원가입시 이메일 인증코드 발급 및 전송 API
     * @param dto MailRequestDto.AuthenticationMail
     * @return ResponseEntity<?>
     */
    @PostMapping("/authentication-mail")
    @Operation(summary = "회원가입시 이메일 인증코드 발급 및 전송 API",
            description = "이메일 인증코드를 생성 및 인증코드를 이메일로 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증코드 발급 및 전송 성공",
                    content = @Content(schema = @Schema(implementation = MailResponseDto.AuthenticationMail.class))),
            @ApiResponse(responseCode = "4000", description = "사용자정의에러코드:등록된 이메일 사용자가 존재합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> authenticationMail(@RequestBody MailRequestDto.AuthenticationMail dto) {
        return ResponseEntity.ok(loginService.authenticationMail(dto));
    }

    /**
     * 회원가입시 이메일 인증코드 일치 여부 확인 API
     * @param dto MailRequestDto.CheckAuthenticationCode
     * @return ResponseEntity<?>
     */
    @PostMapping("/check-code")
    @Operation(summary = "회원가입시 이메일 인증코드 일치 여부 확인 API",
            description = "인증코드가 일치하는지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증결과 전송",
                    content = @Content(schema = @Schema(implementation = MemberResponse.SignInDto.class)))
    })
    public ResponseEntity<?> checkAuthenticationCode(@RequestBody MailRequestDto.CheckAuthenticationCode dto) {
        return ResponseEntity.ok(loginService.checkAuthenticationCode(dto));
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
     * 센터 등록
     * @param centerDto CenterDto
     * @return ResponseEntity<CenterDto>
     */
    @PostMapping("/center-register")
    @Operation(summary = "센터 등록 API", description = "센터 정보를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공",
                    content = @Content(schema = @Schema(implementation = CenterResponseDto.RegisterCenter.class))),
            @ApiResponse(responseCode = "4004", description = "사용자정의에러코드:같은 주소의 센터가 존재",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> registerCenter(@RequestBody CenterRequestDto.RegisterCenter centerDto) {
        return ResponseEntity.ok().body(loginService.registerCenter(centerDto));
    }

    /**
     * 관리자(센터직원) 회원 가입
     * @param signUpDTO CenterManagerRequestDto
     * @return ResponseEntity<CenterManagerResponseDto>
     */
    @PostMapping("/up/manager")
    @Operation(summary = "관리자(센터직원) 회원 가입 API", description = "관리자(센터직원) 개인 계정 회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = CenterManagerResponseDto.SignUpManager.class))),
            @ApiResponse(responseCode = "4000", description = "로그인 아이디가 이미 존재합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "4003", description = "사용자정의에러코드:센터 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> signUpManager(@RequestBody CenterManagerRequestDto.SignUpManager signUpDTO) {
        return ResponseEntity.ok().body(loginService.signUpManager(signUpDTO));
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

}
