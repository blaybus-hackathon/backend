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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Operation(summary = "어르신 정보 수정 API")
    public ResponseEntity<?> updatePatientInfo(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestBody PatientRequestDto.UpdatePatientInfo dto) {
        return ResponseEntity.ok().body(patientService.updatePatientInfo(userDetails, dto));
    }

    @PostMapping("/recruit-helper")
    @Operation(summary = "어르신 요양보호사 공고 등록 API")
    public ResponseEntity<?> recruitHelper(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody PatientRequestDto.RecruitHelper dto) {
        return ResponseEntity.ok().body(patientService.recruitHelper(userDetails, dto));
    }

}
