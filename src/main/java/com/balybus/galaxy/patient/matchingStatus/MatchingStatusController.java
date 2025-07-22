package com.balybus.galaxy.patient.matchingStatus;

import com.balybus.galaxy.global.exception.ErrorResponse;
import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusRequestDto;
import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusResponseDto;
import com.balybus.galaxy.patient.matchingStatus.service.MatchingStatusServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("tient-match-status")
public class MatchingStatusController {
    private final MatchingStatusServiceImpl matchingStatusService;
    
    @Operation(summary = "어르신 매칭 대기 중 리스트 반환 API", description = "매칭 대기 중인 어르신 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반환 성공",
                    content = @Content(schema = @Schema(implementation = MatchingStatusResponseDto.MatchingStatusPatientInfoList.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/waiting-patient-list")
    public ResponseEntity<?> getMatchingWaitingPatientList(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(matchingStatusService.matchingWaitPatientInfoList(userDetails.getUsername()));
    }

    @Operation(summary = "어르신 매칭 중 리스트 반환 API", description = "매칭 중인 어르신 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반환 성공",
                    content = @Content(schema = @Schema(implementation = MatchingStatusResponseDto.MatchingStatusPatientInfoList.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/matching-patient-list")
    public ResponseEntity<?> getMatchingPatientList(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(matchingStatusService.matchingPatientInfoList(userDetails.getUsername()));
    }

    @Operation(summary = "어르신 매칭 완료 리스트 반환 API", description = "매칭 완료 / 거절 상태인 어르신 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반환 성공",
                    content = @Content(schema = @Schema(implementation = MatchingStatusResponseDto.MatchingStatusPatientInfoList.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/matched-patient-list")
    public ResponseEntity<?> getMatchedPatientList(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(matchingStatusService.matchedFinPatientInfoList(userDetails.getUsername()));
    }

    @Operation(summary = "어르신 공고 매칭 상태 변경 API", description = "매칭 완료 / 거절 상태인 어르신 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반환 성공",
                    content = @Content(schema = @Schema(implementation = MatchingStatusResponseDto.UpdatePatientMatchStatus.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("")
    public ResponseEntity<?> updatePatientMatchStatus(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestBody MatchingStatusRequestDto.UpdatePatientMatchStatus dto) {
        return ResponseEntity.ok().body(matchingStatusService.updatePatientMatchStatus(userDetails.getUsername(), dto));
    }

    // 요양보호사 기준 매칭 목록 조회 API
    @Operation(summary = "요양보호사 매칭 요청 목록 조회 API", description = "요양보호사 기준으로 매칭 요청/조율 상태인 공고 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반환 성공",
                    content = @Content(schema = @Schema(implementation = MatchingStatusResponseDto.HelperMatchingList.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "3007", description = "사용자정의에러코드:로그인한 사용자의 권한이 요양보호사가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/helper-matching-request-list")
    public ResponseEntity<?> getHelperMatchingRequestList(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(matchingStatusService.helperMatchingRequestList(userDetails.getUsername()));
    }

    @Operation(summary = "요양보호사 매칭 완료 목록 조회 API", description = "요양보호사 기준으로 매칭 완료 상태인 공고 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반환 성공",
                    content = @Content(schema = @Schema(implementation = MatchingStatusResponseDto.HelperMatchingList.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "3007", description = "사용자정의에러코드:로그인한 사용자의 권한이 요양보호사가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/helper-matching-completed-list")
    public ResponseEntity<?> getHelperMatchingCompletedList(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(matchingStatusService.helperMatchingCompletedList(userDetails.getUsername()));
    }

    // 요양보호사 구분자 기반 매칭 목록 조회 API
    @Operation(summary = "요양보호사 구분자로 매칭 요청 목록 조회 API", description = "요양보호사 구분자를 이용해 매칭 요청/조율 상태인 공고 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반환 성공",
                    content = @Content(schema = @Schema(implementation = MatchingStatusResponseDto.HelperMatchingList.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/helper/{helperId}/matching-request-list")
    public ResponseEntity<?> getHelperMatchingRequestListByHelperId(@PathVariable Long helperId) {
        return ResponseEntity.ok().body(matchingStatusService.helperMatchingRequestListByHelperId(helperId));
    }

    @Operation(summary = "요양보호사 구분자로 매칭 완료 목록 조회 API", description = "요양보호사 구분자를 이용해 매칭 완료 상태인 공고 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반환 성공",
                    content = @Content(schema = @Schema(implementation = MatchingStatusResponseDto.HelperMatchingList.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/helper/{helperId}/matching-completed-list")
    public ResponseEntity<?> getHelperMatchingCompletedListByHelperId(@PathVariable Long helperId) {
        return ResponseEntity.ok().body(matchingStatusService.helperMatchingCompletedListByHelperId(helperId));
    }
}
