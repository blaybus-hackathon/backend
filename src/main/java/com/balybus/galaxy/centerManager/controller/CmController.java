package com.balybus.galaxy.centerManager.controller;

import com.balybus.galaxy.centerManager.dto.CmResponseDto;
import com.balybus.galaxy.centerManager.service.CmServiceImpl;
import com.balybus.galaxy.global.exception.ErrorResponse;
import com.balybus.galaxy.member.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    //센터 관리자 정보 수정
    //센터 정보 수정
}
