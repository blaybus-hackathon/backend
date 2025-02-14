package com.balybus.galaxy.helper.controller;

import com.balybus.galaxy.helper.dto.request.HelperExperienceDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkLocationDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkTimeRequestDTO;
import com.balybus.galaxy.helper.dto.response.AddressResponseDTO;
import com.balybus.galaxy.helper.dto.response.HelperExperienceResponse;
import com.balybus.galaxy.helper.dto.response.HelperWorkLocationReponse;
import com.balybus.galaxy.helper.dto.response.HelperWorkTimeResponse;
import com.balybus.galaxy.helper.serviceImpl.service.HelperServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sign-up")
public class HelperSignUpController {

    private final HelperServiceImpl helperService;


    /**
     * 요양 보호사가 선택한 여러 근무 희망 지역 저장
     * @param helperWorkLocationDTO
     * @return
     */
    @PostMapping("/work-location")
    public ResponseEntity<HelperWorkLocationReponse> workLocation(@RequestBody HelperWorkLocationDTO helperWorkLocationDTO) {
        HelperWorkLocationReponse helperWorkLocationReponse = helperService.workLocationSignUp(helperWorkLocationDTO);
        return ResponseEntity.ok(helperWorkLocationReponse);
    }

    /**
     * 근무 희망 지역 리스트 반환
     * @return
     */
    @GetMapping("/get-all-addr")
    public ResponseEntity<AddressResponseDTO> getFirstAddress() {
        return ResponseEntity.ok(helperService.getAllAddress());
    }

    ////////////

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
