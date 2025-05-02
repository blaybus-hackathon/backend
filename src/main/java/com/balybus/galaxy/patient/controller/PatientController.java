package com.balybus.galaxy.patient.controller;

import com.balybus.galaxy.global.exception.ErrorResponse;
import com.balybus.galaxy.patient.dto.PatientRequestDto;
import com.balybus.galaxy.patient.dto.PatientResponseDto;
import com.balybus.galaxy.patient.service.serviceImpl.PatientServiceImpl;
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
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientServiceImpl patientService;

    @PostMapping("/save")
    @Operation(summary = "어르신 정보 등록 API", description = "어르신 정보를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공",
                    content = @Content(schema = @Schema(implementation = PatientResponseDto.SavePatientInfo.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "1001", description = "사용자정의에러코드:주소값이 잘못 설정되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> savePatientInfo(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestBody PatientRequestDto.SavePatientInfo dto) {
        return ResponseEntity.ok().body(patientService.savePatientInfo(userDetails.getUsername(), dto));
    }

    @PostMapping("/update")
    @Operation(summary = "어르신 정보 수정 API", description = "어르신 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = PatientResponseDto.UpdatePatientInfo.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "8000", description = "사용자정의에러코드:해당 어르신 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7001", description = "사용자정의에러코드:수정 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "1001", description = "사용자정의에러코드:주소값이 잘못 설정되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> updatePatientInfo(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestBody PatientRequestDto.UpdatePatientInfo dto) {
        return ResponseEntity.ok().body(patientService.updatePatientInfo(userDetails.getUsername(), dto));
    }

    @GetMapping("/{patientSeq}/detail")
    @Operation(summary = "어르신 정보 상세 조회 API", description = "어르신 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = PatientResponseDto.GetOnePatientInfo.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "8000", description = "사용자정의에러코드:해당 어르신 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7001", description = "사용자정의에러코드:조회 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getOnePatientInfo(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable("patientSeq") Long patientSeq) {
        return ResponseEntity.ok().body(patientService.getOnePatientInfo(userDetails.getUsername(), patientSeq));
    }

    @GetMapping("/list")
    @Operation(summary = "어르신 리스트 조회 API", description = "어르신 리스트 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = PatientResponseDto.GetPatientList.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "8000", description = "사용자정의에러코드:해당 어르신 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7001", description = "사용자정의에러코드:조회 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getPatientList(@AuthenticationPrincipal UserDetails userDetails,
                                            PatientRequestDto.GetPatientList dto) {
        return ResponseEntity.ok().body(patientService.getPatientList(userDetails.getUsername(), dto));
    }

    @PostMapping("/recruit-helper")
    @Operation(summary = "어르신 요양보호사 공고 등록 API")
    public ResponseEntity<?> recruitHelper(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody PatientRequestDto.RecruitHelper dto) {
        return ResponseEntity.ok().body(patientService.recruitHelper(userDetails, dto));
    }

    @GetMapping("/recruit-list")
    @Operation(summary = "어르신 공고 리스트 조회 API", description = "어르신 공고 리스트 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = PatientResponseDto.GetRecruitList.class))),
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
                                            PatientRequestDto.GetRecruitList dto) {
        return ResponseEntity.ok().body(patientService.getRecruitList(userDetails.getUsername(), dto));
    }



    @GetMapping("/recruit-personal-list")
    @Operation(summary = "어르신 공고 개인 리스트 조회 API", description = "어르신 공고 개인 리스트 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = PatientResponseDto.GetRecruitList.class))),
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
                                            PatientRequestDto.GetRecruitPersonalList dto) {
        return ResponseEntity.ok().body(patientService.getRecruitPersonalList(userDetails.getUsername(), dto));
    }

    @GetMapping("/{patientLogSeq}/recruit-detail")
    @Operation(summary = "어르신 공고 상세 조회 API", description = "어르신 공고를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = PatientResponseDto.GetOneRecruitPatientInfo.class))),
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
        return ResponseEntity.ok().body(patientService.getOneRecruitPatientInfo(userDetails.getUsername(), patientLogSeq));
    }



    @PostMapping("/recruit-update")
    @Operation(summary = "어르신 공고 정보 수정 API", description = "어르신 공고 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = PatientResponseDto.UpdateRecruitPatientInfo.class))),
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
                                               @RequestBody PatientRequestDto.UpdateRecruitPatientInfo dto) {
        return ResponseEntity.ok().body(patientService.updateRecruitPatientInfo(userDetails.getUsername(), dto));
    }

    ////

    @Operation(summary = "어르신 매칭 중 리스트 반환 API", description = "매칭 중인 어르신 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반환 성공",
                    content = @Content(schema = @Schema(implementation = PatientResponseDto.UpdateRecruitPatientInfo.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/get-matching-patient-list")
    public ResponseEntity<?> getMatchingPatientList(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(patientService.matchingPatientInfoList(userDetails.getUsername()));
    }

    @Operation(summary = "어르신 매칭 완료 리스트 반환 API", description = "매칭 완료 / 거절 상태인 어르신 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반환 성공",
                    content = @Content(schema = @Schema(implementation = PatientResponseDto.UpdateRecruitPatientInfo.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/get-matched-patient-list")
    public ResponseEntity<?> getMatchedPatientList(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(patientService.matchedPatientInfoList(userDetails.getUsername()));
    }
}
