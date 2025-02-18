package com.balybus.galaxy.center.Controller;

import com.balybus.galaxy.center.DTO.*;
import com.balybus.galaxy.center.serviceimpl.TblCenterServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/center")
@RequiredArgsConstructor
public class TblCenterController {

    /*센터 회원가입기능 구현*/

    private final TblCenterServiceImpl centerService;

    @PostMapping("/register")
    public ResponseEntity<TblCenterResponseDTO> registerCenter(@RequestBody TblCenterSignUpDTO signUpDTO) {
        TblCenterResponseDTO response = centerService.registerCenter(signUpDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-manager")
    public ResponseEntity<TblCenterManaegerUpdateResponseDTO> updateManagerProfile(@RequestBody TblCenterManagerUpdateDTO tblCenterManagerUpdateDTO, @AuthenticationPrincipal UserDetails userDetails) {
        TblCenterManaegerUpdateResponseDTO tblCenterManegerUpdateResponseDTO = centerService.updateCenterManager(tblCenterManagerUpdateDTO, userDetails);
        return ResponseEntity.ok(tblCenterManegerUpdateResponseDTO);
    }

    @GetMapping("/get-patients")
    public ResponseEntity<List<TblPatientResponseDTO.PatientInfo>> getPatients(@AuthenticationPrincipal UserDetails userDetails) {
        List<TblPatientResponseDTO.PatientInfo> tblPatientResponseDTO = centerService.getPatient(userDetails);
        return ResponseEntity.ok(tblPatientResponseDTO);
    }
}
