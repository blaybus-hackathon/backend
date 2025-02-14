package com.balybus.galaxy.helper.controller;

import com.balybus.galaxy.helper.dto.request.HelperExperienceDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkLocationDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkTimeRequestDTO;
import com.balybus.galaxy.helper.dto.response.HelperExperienceResponse;
import com.balybus.galaxy.helper.dto.response.HelperWorkLocationReponse;
import com.balybus.galaxy.helper.dto.response.HelperWorkTimeResponse;
import com.balybus.galaxy.helper.serviceImpl.service.HelperService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sign-up")
public class HelperSignUpController {

    private final HelperService helperService;


    @PostMapping("/work-location")
    public ResponseEntity<HelperWorkLocationReponse> workLocation(@RequestBody HelperWorkLocationDTO helperWorkLocationDTO) {
        HelperWorkLocationReponse helperWorkLocationReponse = helperService.workLocationSignUp(helperWorkLocationDTO);
        return ResponseEntity.ok(helperWorkLocationReponse);
    }

    @PostMapping("/helper-work-time")
    public ResponseEntity<HelperWorkTimeResponse> workTime(@RequestBody HelperWorkTimeRequestDTO helperWorkTimeDTO) {
        HelperWorkTimeResponse helperWorkTimeResponse = helperService.workTimeSignUp(helperWorkTimeDTO);
        return ResponseEntity.ok(helperWorkTimeResponse);
    }

    @PostMapping("/helper-exp")
    public ResponseEntity<HelperExperienceResponse> helperExp(@RequestBody HelperExperienceDTO helperExperienceDTO) {
        HelperExperienceResponse helperExperienceResponse = helperService.experienceSignUp(helperExperienceDTO);
        return ResponseEntity.ok(helperExperienceResponse);
    }
}
