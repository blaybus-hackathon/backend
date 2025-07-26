package com.balybus.galaxy.patient.recruit;

import com.balybus.galaxy.global.exception.ErrorResponse;
import com.balybus.galaxy.patient.recruit.dto.RecruitRequestDto;
import com.balybus.galaxy.patient.recruit.dto.RecruitResponseDto;
import com.balybus.galaxy.patient.recruit.service.RecruitServiceImpl;
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
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/patient-recruit")
public class RecruitController {
    private final RecruitServiceImpl noticeService;

    @PostMapping("/helper")
    @Operation(summary = "어르신 요양보호사 공고 등록 API")
    public ResponseEntity<?> recruitHelper(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody RecruitRequestDto.RecruitHelper dto) {
        return ResponseEntity.ok().body(noticeService.recruitHelper(userDetails, dto));
    }

    @GetMapping("/list")
    @Operation(summary = "어르신 공고 리스트 조회 API", description = "어르신 공고 리스트 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = RecruitResponseDto.GetRecruitList.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "8000", description = "사용자정의에러코드:해당 어르신 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7001", description = "사용자정의에러코드:조회 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getRecruitList(@AuthenticationPrincipal UserDetails userDetails,
                                            RecruitRequestDto.GetRecruitList dto) {
        return ResponseEntity.ok().body(noticeService.getRecruitList(userDetails.getUsername(), dto));
    }



    @GetMapping("/personal-list")
    @Operation(summary = "어르신 공고 개인 리스트 조회 API", description = "어르신 공고 개인 리스트 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = RecruitResponseDto.GetRecruitList.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "8000", description = "사용자정의에러코드:해당 어르신 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7001", description = "사용자정의에러코드:조회 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getRecruitPersonalList(@AuthenticationPrincipal UserDetails userDetails,
                                                    RecruitRequestDto.GetRecruitPersonalList dto) {
        return ResponseEntity.ok().body(noticeService.getRecruitPersonalList(userDetails.getUsername(), dto));
    }

    @GetMapping("/{patientLogSeq}/detail")
    @Operation(summary = "어르신 공고 상세 조회 API", description = "어르신 공고를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = RecruitResponseDto.GetOneRecruitPatientInfo.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "8001", description = "사용자정의에러코드:NOT_FOUND_PATIENT_RECRUIT",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "8000", description = "사용자정의에러코드:해당 어르신 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7001", description = "사용자정의에러코드:조회 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getOneRecruitPatientInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable("patientLogSeq") Long patientLogSeq) {
        return ResponseEntity.ok().body(noticeService.getOneRecruitPatientInfo(userDetails.getUsername(), patientLogSeq, true));
    }

    @GetMapping("/{patientLogSeq}/detail-helper")
    @Operation(summary = "요양보호사 - 어르신 공고 상세 조회 API", description = "어르신 공고를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = RecruitResponseDto.GetOneRecruitPatientInfo.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "8001", description = "사용자정의에러코드:NOT_FOUND_PATIENT_RECRUIT",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "8000", description = "사용자정의에러코드:해당 어르신 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7001", description = "사용자정의에러코드:조회 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getOneRecruitPatientInfoToHelper(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable("patientLogSeq") Long patientLogSeq) {
        return ResponseEntity.ok().body(noticeService.getOneRecruitPatientInfo(userDetails.getUsername(), patientLogSeq, false));
    }



    @PostMapping("/update")
    @Operation(summary = "어르신 공고 정보 수정 API", description = "어르신 공고 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = RecruitResponseDto.UpdateRecruitPatientInfo.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "8001", description = "사용자정의에러코드:해당 어르신 공고를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "8000", description = "사용자정의에러코드:해당 어르신 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7001", description = "사용자정의에러코드:수정 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "1001", description = "사용자정의에러코드:주소값이 잘못 설정되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> updateRecruitPatientInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestBody RecruitRequestDto.UpdateRecruitPatientInfo dto) {
        return ResponseEntity.ok().body(noticeService.updateRecruitPatientInfo(userDetails.getUsername(), dto));
    }
}
