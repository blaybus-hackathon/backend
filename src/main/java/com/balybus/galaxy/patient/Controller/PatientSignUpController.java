package com.balybus.galaxy.patient.Controller;

import com.balybus.galaxy.patient.dto.PatientDto;
import com.balybus.galaxy.patient.serviceImpl.service.PatientServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/patient")
public class PatientSignUpController {
    private final PatientServiceImpl patientService;

    @PostMapping("/register")
    public ResponseEntity<PatientDto> registerPatient(@RequestBody PatientDto patientDto) {
        PatientDto registeredPatient = patientService.registerPatient(patientDto);
        return ResponseEntity.ok(registeredPatient);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
}
