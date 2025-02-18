//package com.balybus.galaxy.patient.Controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/patients")
//@RequiredArgsConstructor
//public class TblPatientController {
//
//    private final TblPatientServiceImpl patientService;
//
//    @PostMapping("/signup")
//    @ResponseStatus(HttpStatus.CREATED)
//    public TblPatientResponseDTO registerPatient(@RequestBody TblPatientSignUpDTO signUpDTO) {
//        return patientService.registerPatient(signUpDTO);
//    }
//
//    @PostMapping("/login")
//    public TblPatientResponseDTO loginPatient(@RequestBody TblPatientLoginDTO loginDTO) {
//        return patientService.loginPatient(loginDTO);
//    }
//}
