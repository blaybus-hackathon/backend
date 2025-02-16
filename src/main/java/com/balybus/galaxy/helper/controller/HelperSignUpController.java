package com.balybus.galaxy.helper.controller;

import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.dto.request.HelperExperienceDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkLocationDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkTimeRequestDTO;
import com.balybus.galaxy.helper.dto.response.*;
import com.balybus.galaxy.helper.serviceImpl.service.HelperServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/sign-up")
public class HelperSignUpController {

    private final HelperServiceImpl helperService;

    ///////////////////////////////////////

    /**
     * 요양 보호사가 선택한 여러 근무 희망 지역 저장
     * @param helperWorkLocationDTO
     * @return
     */
    @PostMapping("/work-location")
    public ResponseEntity<HelperWorkLocationReponse> workLocation(
            @RequestBody HelperWorkLocationDTO helperWorkLocationDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        HelperWorkLocationReponse helperWorkLocationReponse = helperService.workLocationSignUp(helperWorkLocationDTO, userDetails);
        return ResponseEntity.ok(helperWorkLocationReponse);
    }

    /**
     * 광역시.도 근무 희망지 반환
     * @return
     */
    @GetMapping("/get-first-addr")
    public ResponseEntity<List<TblAddressFirstDTO>> getFirstAddress() {
        List<TblAddressFirstDTO> addressList = helperService.getFirstAddress();
        log.info(addressList.toString());
        return ResponseEntity.ok(addressList);
    }

    /**
     * 시.군.구 근무 희망지 반환
     * @param afSeq
     * @return
     */
    @GetMapping("/second/{afSeq}")
    public ResponseEntity<List<TblAddressSecondDTO>> getAddressSecond(@PathVariable Long afSeq) {
        List<TblAddressSecondDTO> addressSeconds = helperService.getAddressSecondByFirstId(afSeq);
        return ResponseEntity.ok(addressSeconds);
    }

    /**
     * 읍.면.동 근무 희망지 반환
     * @param asSeq
     * @return
     */
    @GetMapping("/third/{asSeq}")
    public ResponseEntity<List<TblAddressThirdDTO>> getAddressThird(@PathVariable Long asSeq) {
        List<TblAddressThirdDTO> thirdAddresses = helperService.getThirdAddressBySecondId(asSeq);
        return ResponseEntity.ok(thirdAddresses);
    }

    //////////////////////////////////////

    @PostMapping("/helper-work-time")
    public ResponseEntity<HelperWorkTimeResponse> workTime(
            @RequestBody HelperWorkTimeRequestDTO helperWorkTimeDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        HelperWorkTimeResponse helperWorkTimeResponse = helperService.workTimeSignUp(helperWorkTimeDTO, userDetails);
        return ResponseEntity.ok(helperWorkTimeResponse);
    }

    @PostMapping("/helper-exp")
    public ResponseEntity<HelperExperienceResponse> helperExp(
            @RequestBody HelperExperienceDTO helperExperienceDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        HelperExperienceResponse helperExperienceResponse = helperService.experienceSignUp(helperExperienceDTO, userDetails);
        return ResponseEntity.ok(helperExperienceResponse);
    }
}
