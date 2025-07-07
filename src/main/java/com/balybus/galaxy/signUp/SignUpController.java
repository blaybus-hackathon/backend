package com.balybus.galaxy.signUp;

import com.balybus.galaxy.global.domain.tblCenter.dto.CenterRequestDto;
import com.balybus.galaxy.global.domain.tblCenter.dto.CenterResponseDto;
import com.balybus.galaxy.global.domain.tblCenterManager.dto.CenterManagerRequestDto;
import com.balybus.galaxy.global.domain.tblCenterManager.dto.CenterManagerResponseDto;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ErrorResponse;
import com.balybus.galaxy.global.utils.mail.dto.MailRequestDto;
import com.balybus.galaxy.global.utils.mail.dto.MailResponseDto;
import com.balybus.galaxy.login.classic.dto.request.HelperCertDTO;
import com.balybus.galaxy.login.classic.dto.request.SignUpDTO;
import com.balybus.galaxy.member.dto.response.MemberResponse;
import com.balybus.galaxy.signUp.service.SignUpServiceImpl;
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
@RequestMapping("/api/sign-up")
public class SignUpController {
    private final SignUpServiceImpl signUpService;

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
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "4009", description = "사용자정의에러코드:10분 후에 이메일 전송 요청이 가능합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> authenticationMail(@RequestBody MailRequestDto.AuthenticationMail dto) {
        return ResponseEntity.ok(signUpService.authenticationMail(dto));
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
        return ResponseEntity.ok(signUpService.checkAuthenticationCode(dto));
    }


    /**
     * 요양 보호사 회원 가입
     */
    @Operation(summary = "요양 보호사 회원 가입", description = "요양 보호사 회원 정보를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 가입 성공, 유효하지 않은 자격증을 반환합니다.",
                    content = @Content(schema = @Schema(implementation = HelperCertDTO.class))),
            @ApiResponse(responseCode = "4001", description = "회원가입 정보를 확인해 주세요."),
            @ApiResponse(responseCode = "4000", description = "로그인 아이디가 이미 존재합니다."),
            @ApiResponse(responseCode = "4010", description = "인증되지 않은 이메일입니다.")
    })
    @PostMapping("/helper")
    public ResponseEntity<?> signUpHelper(@RequestBody SignUpDTO signUpDTO) {
        if(signUpDTO.hasNullDataBeforeSignUp())
            throw new BadRequestException(SIGNUP_INFO_NULL);

        return ResponseEntity.ok(signUpService.signUpHelper(signUpDTO));
    }



    /**
     * 센터 리스트 조회
     * @param centerDto CenterRequestDto.GetCenterList
     * @return ResponseEntity<CenterResponseDto.GetCenterList>
     */
    @GetMapping("/center-list")
    @Operation(summary = "센터 리스트 조회 API", description = "센터 리스트 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = CenterResponseDto.GetCenterList.class)))
    })
    public ResponseEntity<?> getCenterList(CenterRequestDto.GetCenterList centerDto) {
        return ResponseEntity.ok().body(signUpService.getCenterList(centerDto));
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
        return ResponseEntity.ok().body(signUpService.registerCenter(centerDto));
    }

    /**
     * 관리자(센터직원) 회원 가입
     * @param signUpDTO CenterManagerRequestDto
     * @return ResponseEntity<CenterManagerResponseDto>
     */
    @PostMapping("/manager")
    @Operation(summary = "관리자(센터직원) 회원 가입 API", description = "관리자(센터직원) 개인 계정 회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = CenterManagerResponseDto.SignUpManager.class))),
            @ApiResponse(responseCode = "4000", description = "로그인 아이디가 이미 존재합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "4003", description = "사용자정의에러코드:센터 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "4010", description = "사용자정의에러코드:인증되지 않은 이메일입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> signUpManager(@RequestBody CenterManagerRequestDto.SignUpManager signUpDTO) {
        return ResponseEntity.ok().body(signUpService.signUpManager(signUpDTO));
    }
}
