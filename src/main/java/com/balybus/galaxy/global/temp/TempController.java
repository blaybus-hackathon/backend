package com.balybus.galaxy.global.temp;

import com.balybus.galaxy.domain.tblImg.TblImgServiceImpl;
import com.balybus.galaxy.domain.tblImg.dto.ImgRequestDto;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/protected")
public class TempController {
    private final TempService tempService;
    private final TblImgServiceImpl imgService;

    @GetMapping("/user")
    public String getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return "Authenticated user: " + userDetails.getUsername();
    }

    @PostMapping("/upload-img")
    @Operation(summary = "파일 업로드 API",
            description = "사용자가 이미지를 업로드하고 서버에 저장하는 기능을 제공합니다. " +
                    "이미지 파일을 multipart/form-data로 전송해야 하며, 성공적으로 업로드되면 이미지 구분자(imgSeq) 리스트가 반환됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "파일 저장 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.SignInDto.class))),
            @ApiResponse(responseCode = "5000", description = "예상하지 못한 서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "6000", description = "사용자정의에러코드:파일 업로드 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> uploadImg(@AuthenticationPrincipal UserDetails userDetails,
                                       ImgRequestDto.UploadImg dto) {
        return ResponseEntity.ok().body(imgService.uploadImg(userDetails, dto));
    }

    @PostMapping("/authentication-mail")
    public ResponseEntity<?> authenticationMail(@AuthenticationPrincipal UserDetails userDetails) {
        tempService.authenticationMail(userDetails.getUsername());
        return ResponseEntity.ok("SUCCESS");
    }
}
