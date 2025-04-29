package com.balybus.galaxy.helper.controller;

import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.dto.request.*;
import com.balybus.galaxy.helper.dto.response.*;
import com.balybus.galaxy.helper.serviceImpl.service.HelperServiceImpl;
import com.balybus.galaxy.login.dto.request.HelperCertDTO;
import com.balybus.galaxy.login.dto.response.AccessTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "요양 보호사 정보 모두 보기", description = "현재 로그인한 요양 보호사 정보 모두 보기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요양 보호사 정보 가져오기 성공",
                content = @Content(schema = @Schema(implementation = HelperResponse.class))),
            @ApiResponse(responseCode = "3000", description = "요양 보호사 테이블을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "3010", description = "로그인한 회원을 찾을 수 없습니다."),
    })
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
    @Operation(summary = "요양 보호사 희망 급여 업데이트", description = "현재 로그인한 요양 보호사 희망 급여 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요양 보호사 희망 급여 업데이트 성공",
                    content = @Content(schema = @Schema(implementation = WageUpdateResponse.class))),
            @ApiResponse(responseCode = "3000", description = "요양 보호사 테이블을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "3010", description = "로그인한 회원을 찾을 수 없습니다.")
    })
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
    @Operation(summary = "요양 보호사 프로필 업데이트", description = "현재 로그인한 요양 보호사 프로필 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필이 정상적으로 업데이트 되었습니다.",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "3000", description = "요양 보호사 테이블을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "3010", description = "로그인한 회원을 찾을 수 없습니다.")
    })
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
    @Operation(summary = "요양 보호사가 선택한 여러 근무 희망 지역 저장", description = "현재 로그인한 요양 보호사가 선택한 여러 근무 희망 지역 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "근무 희망 지역이 성공적으로 저장되었습니다.",
                    content = @Content(schema = @Schema(implementation = HelperWorkLocationReponse.class))),
            @ApiResponse(responseCode = "3000", description = "요양 보호사 테이블을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "3008", description = "잘못된 주소 목록"),
            @ApiResponse(responseCode = "3010", description = "로그인한 회원을 찾을 수 없습니다."),
    })
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
    @Operation(summary = "광역시.도 목록 조회", description = "모든 광역시.도 주소 목록을 반환 합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "시.도 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TblAddressFirstResponse.class))
                    )
            )
    })
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
    @Operation(summary = "광역시.군.구 목록 조회", description = "광역시.도.구 주소 목록을 반환 합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "시.군.구 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TblAddressSecondResponse.class))
                    )
            )
    })
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
    @Operation(summary = "읍.면.동 근무 희망지 반환", description = "읍.면.동 근무 희망지 목록을 반환 합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "읍.면.동 근무 희망지 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TblAddressSecondResponse.class))
                    )
            )
    })
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
    @Operation(summary = "요양 보호사 근무 가능 시간 저장", description = "현재 로그인한 요양 보호사가 근무 가능한 시간 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "근무 가능 시간이 성공적으로 저장되었습니다.",
                    content = @Content(schema = @Schema(implementation = HelperWorkTimeResponse.class))),
            @ApiResponse(responseCode = "3000", description = "요양 보호사 테이블을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "3009", description = "중복된 근무 가능 시간 주소가 존재합니다."),
            @ApiResponse(responseCode = "3010", description = "로그인한 회원을 찾을 수 없습니다."),
    })
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
    @Operation(summary = "요양 보호사 경력 저장", description = "현재 로그인한 요양 보호사 경력 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요양 보호사의 경력이 성공적으로 저장되었습니다.",
                    content = @Content(schema = @Schema(implementation = HelperExperienceResponse.class))),
            @ApiResponse(responseCode = "3000", description = "요양 보호사 테이블을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "3004", description = "요양 보호사 경력 회원 가입 정보를 다시 확인해주세요."),
            @ApiResponse(responseCode = "3006", description = "해당 요양 보호사는 이미 등록된 근무 경력이 있습니다."),
            @ApiResponse(responseCode = "3010", description = "로그인한 회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "5000", description = "서버에서 에러가 발생하였습니다.")
    })
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
    @Operation(summary = "요양 보호사 검색", description = "조건에 맞는 요양 보호사 검색")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조건에 맞는 요양 보호사가 모두 검색되었습니다.",
                    content = @Content(schema = @Schema(implementation = HelperSearchResponse.class))),
            @ApiResponse(responseCode = "3010", description = "로그인한 회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "3011", description = "요양 보호사 나이 계산 중 알 수 없는 에러가 발생 했습니다."),
    })
    @PostMapping("/search-helper")
    public ResponseEntity<HelperSearchResponse> searchHelper(@RequestBody HelperSearchDTO helperSearchDTO) {
        HelperSearchResponse helperSearchResponse = helperService.helperSearch(helperSearchDTO);
        return ResponseEntity.ok(helperSearchResponse);
    }

    /**
     * 요양 보호사 Q-Net 자격증 검증 로직
     * @param helperCertDTO 요양 보호사 자격증 정보
     * @return
     */
    @Operation(summary = "요양 보호사 자격증 검증", description = "요양 보호사 관련 자격증 유효성을 Q-Net에서 검증하기ㅏ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요양 보호사 관련 자격증이 인증되었습니다.",
                    content = @Content(schema = @Schema(implementation = HelperSearchResponse.class))),
            @ApiResponse(responseCode = "3010", description = "로그인한 회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "3000", description = "요양 보호사 테이블을 찾을 수 없습니다."),
    })
    @PostMapping("/certi-verify")
    public ResponseEntity<Map<String, String>> certiVerify(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody List<HelperCertDTO> helperCertDTO) {
        Map<String, String> result = helperService.saveCertificateByQNet(helperCertDTO, userDetails);
        return ResponseEntity.ok(result);
    }
}
