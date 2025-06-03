//package com.balybus.galaxy.global.temp;
//
//import com.balybus.galaxy.global.domain.tblImg.service.TblImgServiceImpl;
//import com.balybus.galaxy.global.domain.tblImg.dto.ImgRequestDto;
//import com.balybus.galaxy.global.exception.ErrorResponse;
//import com.balybus.galaxy.member.dto.response.MemberResponse;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/protected")
//public class TempController {
//    private final TempService tempService;
//    private final TblImgServiceImpl imgService;
//
//    @Operation(summary = "사용자 이름 반환",
//            description = "로그인한 사용자 이름 반환")
//    @GetMapping("/user")
//    public String getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
//        return "Authenticated user: " + userDetails.getUsername();
//    }
//
////    @PostMapping("/upload-img")
//    @Operation(summary = "파일 업로드 API",
//            description = "사용자가 이미지를 업로드하고 서버에 저장하는 기능을 제공합니다. " +
//                    "이미지 파일을 multipart/form-data로 전송해야 하며, 성공적으로 업로드되면 이미지 구분자(imgSeq) 리스트가 반환됩니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "파일 저장 성공",
//                    content = @Content(schema = @Schema(implementation = MemberResponse.SignInDto.class))),
//            @ApiResponse(responseCode = "5000", description = "예상하지 못한 서버 에러",
//                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "6000", description = "사용자정의에러코드:파일 업로드 실패",
//                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//    })
//    public ResponseEntity<?> uploadImg(@AuthenticationPrincipal UserDetails userDetails,
//                                       ImgRequestDto.UploadImg dto) {
//        return ResponseEntity.ok().body(imgService.uploadImg(dto.getPhotoFiles()));
//    }
//
//    /////////////
//    // aws s3-multipart 구현
//    @Operation(summary = "파일 업로드 API",
//            description = "AWS S3 Multipart Presigned 사진 업로드" )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "파일 저장 성공",
//                    content = @Content(schema = @Schema(implementation = MemberResponse.SignInDto.class)))
//    })
//    @PostMapping("/presigned-url")
//    public ResponseEntity<Map<String, String>> initiateUpload(@RequestBody ImgRequestDto.PreSignedUrlCreateRequest request) {
//        Map<String, String> url = imgService.getPresignedUrl("images", request.getFileName());
//        return ResponseEntity.ok(url);
//    }
//
//    ///////////////
//    @Operation(summary = "이메일 인증 요청", description = "사용자의 이메일로 인증 코드를 전송합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "이메일 전송 성공",
//                    content = @Content(schema = @Schema(implementation = String.class)))
//    })
//    @PostMapping("/authentication-mail")
//    public ResponseEntity<?> authenticationMail(@AuthenticationPrincipal UserDetails userDetails) {
//        tempService.authenticationMail(userDetails.getUsername());
//        return ResponseEntity.ok("SUCCESS");
//    }
//
//    @Operation(summary = "요양보호사 매칭 결과 이메일로 전송", description = "어르신의 요양보호사 매칭 결과를 이메일로 전송합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "추천 리스트 메일 전송 성공",
//                    content = @Content(schema = @Schema(implementation = String.class)))
//    })
//    @PostMapping("/matching-mail")
//    public ResponseEntity<?> matchingMail(@AuthenticationPrincipal UserDetails userDetails) {
//        tempService.matchingMail();
//        return ResponseEntity.ok("SUCCESS");
//    }
//
//    @Operation(summary = "요양보호사 자동 매칭 실행", description = "어르신 조건에 맞는 요양보호사들을 추천하고 관리자 이메일로 리스트를 전송합니다.")
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200", description = "매칭 및 이메일 전송 성공",
//                    content = @Content(schema = @Schema(implementation = String.class))
//            )
//    })
//    @PostMapping("/matching")
//    public ResponseEntity<?> matching(@AuthenticationPrincipal UserDetails userDetails) {
//        tempService.matching();
//        return ResponseEntity.ok("SUCCESS");
//    }
//}
