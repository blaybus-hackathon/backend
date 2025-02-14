package com.balybus.galaxy.helper.controller;

import com.balybus.galaxy.helper.dto.request.HelperWorkLocationDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkTimeRequestDTO;
import com.balybus.galaxy.helper.dto.response.HelperWorkTimeResponse;
import com.balybus.galaxy.helper.serviceImpl.service.HelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HelperController {

    private final HelperService helperService;


    @PostMapping("/work-location")
    public String workLocation(@RequestBody HelperWorkLocationDTO helperWorkLocationDTO) {
        return "";
    }

    @PostMapping("/work-time")
    public ResponseEntity<HelperWorkTimeResponse> workTime(@RequestBody HelperWorkTimeRequestDTO helperWorkTimeDTO) {
        HelperWorkTimeResponse helperWorkTimeResponse = helperService.workTimeSignUp(helperWorkTimeDTO);
        return ResponseEntity.ok(helperWorkTimeResponse);
    }

    @PostMapping("/helper-exp")
    public String helperExp() {
        return "";
    }
}
