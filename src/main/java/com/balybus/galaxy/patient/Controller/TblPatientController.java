//package com.balybus.galaxy.patient.Controller;
//
//import com.balybus.galaxy.patient.DTO.TblPatientResponseDTO;
//import com.balybus.galaxy.patient.DTO.TblPatientSignUpDTO;
//import com.balybus.galaxy.patient.DTO.TblPatientLoginDTO;
//import com.balybus.galaxy.patient.serviceimpl.service.TblPatientServiceImpl;
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
