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
@RequestMapping("/api/patient-match-status")
public class MatchingStatusController {
    private final MatchingStatusServiceImpl matchingStatusService;
    @Operation(summary = "어르신 매칭 대기 중 리스트 반환 API", description = "매칭 대기 중인 어르신 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반환 성공",
                    content = @Content(schema = @Schema(implementation = MatchingStatusResponseDto.MatchingWaitPatientInfoList.class))),
            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/waiting-patient-list")
    public ResponseEntity<?> getMatchingWaitingPatientList(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(matchingStatusService.matchingWaitPatientInfoList(userDetails.getUsername()));
    }

//    @Operation(summary = "어르신 매칭 중 리스트 반환 API", description = "매칭 중인 어르신 정보를 반환합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "반환 성공",
//                    content = @Content(schema = @Schema(implementation = MatchingStatusResponseDto.MatchingPatientInfoList.class))),
//            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
//                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
//                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//    })
//    @GetMapping("/matching-patient-list")
//    public ResponseEntity<?> getMatchingPatientList(@AuthenticationPrincipal UserDetails userDetails) {
//        return ResponseEntity.ok().body(matchingStatusService.matchingPatientInfoList(userDetails.getUsername()));
//    }
//
//    @Operation(summary = "어르신 매칭 완료 리스트 반환 API", description = "매칭 완료 / 거절 상태인 어르신 정보를 반환합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "반환 성공",
//                    content = @Content(schema = @Schema(implementation = MatchingStatusResponseDto.MatchedPatientInfoList.class))),
//            @ApiResponse(responseCode = "4008", description = "사용자정의에러코드:로그인이 정보 없음(쿠키 없음)",
//                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "7000", description = "사용자정의에러코드:로그인한 사용자의 권한이 매니저가 아닙니다.",
//                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//    })
//    @GetMapping("/matched-patient-list")
//    public ResponseEntity<?> getMatchedPatientList(@AuthenticationPrincipal UserDetails userDetails) {
//        return ResponseEntity.ok().body(matchingStatusService.matchedPatientInfoList(userDetails.getUsername()));
//    }


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
}
