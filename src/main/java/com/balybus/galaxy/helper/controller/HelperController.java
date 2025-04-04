package com.balybus.galaxy.helper.controller;

import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.dto.request.*;
import com.balybus.galaxy.helper.dto.response.*;
import com.balybus.galaxy.helper.serviceImpl.service.HelperServiceImpl;
import com.balybus.galaxy.login.dto.request.HelperCertDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class HelperController {

    private final HelperServiceImpl helperService;

    /**
     * 현재 로그인한 요양 보호사 정보 모두 보기
     * @param userDetails
     * @return
     */
    @GetMapping("/get-helper-info")
    public ResponseEntity<HelperResponse> getHelperInfo(@AuthenticationPrincipal UserDetails userDetails) {
        HelperResponse helperResponse = helperService.getAllHelperInfo(userDetails);
        return ResponseEntity.ok(helperResponse);
    }

    /**
     * 희망 급여 업데이트
     * @param userDetails
     * @param wageUpdateDTO
     * @return
     */
    @PutMapping("/update-wage")
    public ResponseEntity<WageUpdateResponse> updateWage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody WageUpdateDTO wageUpdateDTO) {
        WageUpdateResponse wageUpdateResponse = helperService.updateWage(userDetails, wageUpdateDTO);
        return ResponseEntity.ok(wageUpdateResponse);
    }

    /**
     * 요양 보호사 프로필 업데이트
     * @param helperProfileDTO
     * @param userDetails
     * @return
     */
    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@RequestBody HelperProfileDTO helperProfileDTO, @AuthenticationPrincipal UserDetails userDetails) {
        helperService.updateProfile(userDetails, helperProfileDTO);
        return ResponseEntity.ok("프로필이 정상적으로 업데이트 되었습니다.");
    }


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
    public ResponseEntity<List<TblAddressFirstResponse>> getFirstAddress() {
        List<TblAddressFirstResponse> addressList = helperService.getFirstAddress();
        log.info(addressList.toString());
        return ResponseEntity.ok(addressList);
    }

    /**
     * 시.군.구 근무 희망지 반환
     * @param afSeq
     * @return
     */
    @GetMapping("/second/{afSeq}")
    public ResponseEntity<List<TblAddressSecondResponse>> getAddressSecond(@PathVariable Long afSeq) {
        List<TblAddressSecondResponse> addressSeconds = helperService.getAddressSecondByFirstId(afSeq);
        return ResponseEntity.ok(addressSeconds);
    }

    /**
     * 읍.면.동 근무 희망지 반환
     * @param asSeq
     * @return
     */
    @GetMapping("/third/{asSeq}")
    public ResponseEntity<List<TblAddressThirdResponse>> getAddressThird(@PathVariable Long asSeq) {
        List<TblAddressThirdResponse> thirdAddresses = helperService.getThirdAddressBySecondId(asSeq);
        return ResponseEntity.ok(thirdAddresses);
    }

    //////////////////////////////////////

    /**
     * 요양 보호사 근무 가능 시간 저장
     * @param helperWorkTimeDTO
     * @param userDetails
     * @return
     */
    @PostMapping("/helper-work-time")
    public ResponseEntity<HelperWorkTimeResponse> workTime(
            @RequestBody HelperWorkTimeRequestDTO helperWorkTimeDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        HelperWorkTimeResponse helperWorkTimeResponse = helperService.workTimeSignUp(helperWorkTimeDTO, userDetails);
        return ResponseEntity.ok(helperWorkTimeResponse);
    }

    /**
     * 요양 보호사 경력 저장
     * @param helperExperienceDTO
     * @param userDetails
     * @return
     */
    @PostMapping("/helper-exp")
    public ResponseEntity<HelperExperienceResponse> helperExp(
            @RequestBody HelperExperienceDTO helperExperienceDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        HelperExperienceResponse helperExperienceResponse = helperService.experienceSignUp(helperExperienceDTO, userDetails);
        return ResponseEntity.ok(helperExperienceResponse);
    }

    /**
     * 요양 보호사 조회
     * @param helperSearchDTO
     * @return
     */
    @PostMapping("/search-helper")
    public ResponseEntity<HelperSearchResponse> searchHelper(@RequestBody HelperSearchDTO helperSearchDTO) {
        HelperSearchResponse helperSearchResponse = helperService.helperSearch(helperSearchDTO);
        return ResponseEntity.ok(helperSearchResponse);
    }

    @PostMapping("/certi-verify")
    public ResponseEntity<Map<String, String>> certiVerify(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody List<HelperCertDTO> helperCertDTO) {
        Map<String, String> result = helperService.saveCertificateByQNet(helperCertDTO, userDetails);
        return ResponseEntity.ok(result);
    }
}
