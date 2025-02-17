package com.balybus.galaxy.global.common;

import com.balybus.galaxy.domain.tblImg.dto.ImgRequestDto;
import com.balybus.galaxy.global.exception.ErrorResponse;
import com.balybus.galaxy.member.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/cmn")
public class CommonController {
    private final CommonServiceImpl commonService;

    @PostMapping("/upload-img")
    @Operation(summary = "프로필 이미지 변경 및 업로드 API",
            description = "주체(요양보호사, 관리자, 어르신)의 프로필 이미지를 업로드하고 서버에 저장하는 기능을 제공합니다. " +
                    "이미지 파일은 multipart/form-data로 전송해야 하며, 성공적으로 업로드되면 이미지 구분자(imgSeq)가 반환됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 이미지 변경 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.SignInDto.class))),
            @ApiResponse(responseCode = "4005", description = "이미지 등록 권한 없음",
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
    public ResponseEntity<?> uploadUserImg(@AuthenticationPrincipal UserDetails userDetails,
                                       ImgRequestDto.uploadUserImg dto) {
        return ResponseEntity.ok().body(commonService.uploadUserImg(userDetails, dto));
    }
}
