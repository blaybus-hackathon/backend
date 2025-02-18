package com.balybus.galaxy.Center.Controller;

import com.balybus.galaxy.Center.dto.CenterDto;
import com.balybus.galaxy.Center.serviceimpl.CenterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/center")
public class CenterSingUpController {

    private final CenterService centerService;


    @PostMapping("/register")
    public ResponseEntity<CenterDto> registerCenter(@RequestBody CenterDto centerDto) {
        CenterDto registeredCenter = centerService.registerCenter(centerDto);
        return ResponseEntity.ok(registeredCenter);
    }
}