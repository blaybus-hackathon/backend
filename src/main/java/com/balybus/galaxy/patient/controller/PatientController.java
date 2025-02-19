package com.balybus.galaxy.patient.controller;

import com.balybus.galaxy.patient.dto.PatientRequestDto;
import com.balybus.galaxy.patient.service.serviceImpl.PatientServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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

}
