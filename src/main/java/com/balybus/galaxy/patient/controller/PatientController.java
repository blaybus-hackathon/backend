package com.balybus.galaxy.patient.controller;

import com.balybus.galaxy.patient.dto.PatientRequestDto;
import com.balybus.galaxy.patient.service.serviceImpl.PatientServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "어르신 정보 등록 API")
    public ResponseEntity<?> savePatientInfo(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestBody PatientRequestDto.SavePatientInfo dto) {
        return ResponseEntity.ok().body(patientService.savePatientInfo(userDetails, dto));
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

    /*// 매칭중 리스트 조회
    @GetMapping("/matching")
    @Operation(summary = "매칭중 어르신 리스트 조회 API")
    public ResponseEntity<?> getMatchingPatientList(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(patientService.getMatchingPatientList(userDetails));
    }

    // 매칭완료 리스트 조회
    @GetMapping("/matching-completed")
    @Operation(summary = "매칭완료 어르신 리스트 조회 API")
    public ResponseEntity<?> getCompletedMatchingPatientList(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(patientService.getCompletedMatchingPatientList(userDetails));
    }

    // 매칭 완료 상태로 변경
    @PostMapping("/matching/{patientLogId}/complete")
    @Operation(summary = "매칭 완료 상태로 변경 API")
    public ResponseEntity<?> completeMatching(@PathVariable Long patientLogId) {
        patientService.completeMatching(patientLogId);
        return ResponseEntity.noContent().build();
    }*/
}
