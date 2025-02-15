package com.balybus.galaxy.manager.controller;

import com.balybus.galaxy.manager.DTO.TblManagerResponseDTO;
import com.balybus.galaxy.manager.DTO.TblManagerSignUpDTO;
import com.balybus.galaxy.manager.serviceimpl.service.TblManagerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sign-up")
public class TblManagerSignUpController {

    private final TblManagerServiceImpl managerService;

    @PostMapping("/manager-info")
    public ResponseEntity<TblManagerResponseDTO> managerInfo(@RequestBody TblManagerSignUpDTO signUpDTO) {
        TblManagerResponseDTO response = managerService.registerManager(signUpDTO);
        return ResponseEntity.ok(response);
    }
}
