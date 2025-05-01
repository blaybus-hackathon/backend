package com.balybus.galaxy.centerManager.controller;

import com.balybus.galaxy.centerManager.dto.CmRequestDto;
import com.balybus.galaxy.centerManager.dto.CmResponseDto;
import com.balybus.galaxy.centerManager.service.CmServiceImpl;
import com.balybus.galaxy.domain.tblCenter.dto.CenterRequestDto;
import com.balybus.galaxy.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/center-manager")
public class CmController {
    private final CmServiceImpl cmService;

    /**
     * 센터 관리자 정보 조회
     * @param userDetails UserDetails
     * @return ResponseEntity<CmResponseDto.GetOneManager>
     */
    @GetMapping("/find")
    @Operation(summary="센터 관리자 정보 조회 API", description = "로그인되어 있는 관리자 계정을 기준으로 로그인 사용자 정보를 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = CmResponseDto.GetOneManager.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getOneManager(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok().body(cmService.getOneManager(userDetails.getUsername()));
    }

    /**
     * 센터 관리자 정보 수정
     * @param userDetails UserDetails
     * @param dto CmRequestDto.UpdateManager
     * @return ResponseEntity<CmResponseDto.UpdateManager>
     */
    @PostMapping("/update")
    @Operation(summary="센터 관리자 정보 수정 API", description = "센터 관리자의 직책과 이미지 정보만을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = CmResponseDto.GetOneManager.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7001", description = "사용자정의에러코드:수정 권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5000", description = "예상하지 못한 서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "6000", description = "사용자정의에러코드:파일 업로드 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "6001", description = "사용자정의에러코드:삭제 파일을 찾지 못함",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "6002", description = "사용자정의에러코드:2개 이상의 파일이 전송됨",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "6003", description = "사용자정의에러코드:이미지를 등록하려는 주체 정보 조회 불가",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> updateManager(@AuthenticationPrincipal UserDetails userDetails,
                                           CmRequestDto.UpdateManager dto){
        return ResponseEntity.ok().body(cmService.updateManager(userDetails.getUsername(), dto));
    }

    /**
     * 센터 정보 수정
     * @param userDetails UserDetails
     * @param dto CenterRequestDto.UpdateCenter
     * @return ResponseEntity<CmResponseDto.UpdateCenter>
     */
    @PostMapping("/center-update")
    @Operation(summary="센터 정보 수정 API", description = "센터 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = CmResponseDto.GetOneManager.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7001", description = "사용자정의에러코드:수정 권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "4003", description = "사용자정의에러코드:센터 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "4004", description = "사용자정의에러코드:해당 주소로 등록된 센터 정보가 이미 존재합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> updateCenter(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestBody CenterRequestDto.UpdateCenter dto){
        return ResponseEntity.ok().body(cmService.updateCenter(userDetails.getUsername(), dto));
    }
}
