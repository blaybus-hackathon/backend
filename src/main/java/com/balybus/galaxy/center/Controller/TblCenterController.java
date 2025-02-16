//package com.balybus.galaxy.center.Controller;
//
//import com.balybus.galaxy.center.DTO.TblCenterResponseDTO;
//import com.balybus.galaxy.center.DTO.TblCenterSignUpDTO;
//import com.balybus.galaxy.center.serviceimpl.TblCenterServiceImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/center")
//@RequiredArgsConstructor
//public class TblCenterController {
//
//    /*센터 회원가입기능 구현*/
//
//    private final TblCenterServiceImpl centerService;
//
//    @PostMapping("/register")
//    public ResponseEntity<TblCenterResponseDTO> registerCenter(@RequestBody TblCenterSignUpDTO signUpDTO) {
//        TblCenterResponseDTO response = centerService.registerCenter(signUpDTO);
//        return ResponseEntity.ok(response);
//    }
//}
