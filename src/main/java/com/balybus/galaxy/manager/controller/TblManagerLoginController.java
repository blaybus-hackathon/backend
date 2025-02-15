package com.balybus.galaxy.manager.controller;

import com.balybus.galaxy.manager.DTO.TblManagerLoginDTO;
import com.balybus.galaxy.manager.DTO.TblManagerResponseDTO;
import com.balybus.galaxy.manager.serviceimpl.service.TblManagerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class TblManagerLoginController {

    private final TblManagerServiceImpl managerService;

    @PostMapping("/manager-login")
    public ResponseEntity<TblManagerResponseDTO> login(@RequestBody TblManagerLoginDTO loginDTO) {
        TblManagerResponseDTO response = managerService.loginManager(loginDTO);
        return ResponseEntity.ok(response);
    }
}
