package com.balybus.galaxy.global.utils.code;

import com.balybus.galaxy.global.domain.tblCare.dto.CareRequestDto;
import com.balybus.galaxy.global.domain.tblCare.service.TblCareServiceImpl;
import com.balybus.galaxy.global.domain.tblImg.dto.ImgRequestDto;
import com.balybus.galaxy.global.exception.ErrorResponse;
import com.balybus.galaxy.global.utils.code.CodeServiceImpl;
import com.balybus.galaxy.login.classic.domain.type.RoleType;
import com.balybus.galaxy.member.dto.response.MemberResponse;
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
@RequestMapping("/api/cmn")
public class CodeController {

    private final CodeServiceImpl commonService;
    private final TblCareServiceImpl careService;

    @GetMapping("/all-care-list")
    @Operation(summary = "어르신 정보 관련 코드화 리스트 조회 API",
            description = "어르신 정보에 사용되는 코드화된 데이터 리스트를 반환합니다." +
                    "(근무종류 / 복리후생 / 장기요양등급 / 치매증상 / 동거인 여부 / 성별" +
                    " / 어르신 필요 서비스 - 식사보조 / 어르신 필요 서비스 - 배변보조" +
                    " / 어르신 필요 서비스 - 이동보조 / 어르신 필요 서비스 - 일상생활)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.SignInDto.class)))
    })
    public ResponseEntity<?> getAllCodeList() {
        return ResponseEntity.ok().body(careService.getAllCodeList());
    }

    @GetMapping("/part-service-care-list")
    @Operation(summary = "어르신 정보 중 어르신 필요 서비스 코드화 리스트 조회 API",
            description = "어르신 정보에 사용되는 코드화된 데이터 리스트를 반환합니다." +
                    "(어르신 필요 서비스 - 식사보조 / 어르신 필요 서비스 - 배변보조" +
                    " / 어르신 필요 서비스 - 이동보조 / 어르신 필요 서비스 - 일상생활)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.SignInDto.class)))
    })
    public ResponseEntity<?> getServiceCodeList() {
        return ResponseEntity.ok().body(careService.getServiceCodeList());
    }



    @PostMapping("/part-request-care-list")
    @Operation(summary = "어르신 정보 중 요청한 내용에 대한 코드화 리스트 조회 API",
            description = "필요한 종류의 정보를 리스트로 정리해 전달하면 각각에 대한 코드화된 데이터 리스트를 반환합니다." +
                    "근무종류(WORK_TYPE) / 복리후생(WELFARE) / 장기요양등급(CARE_LEVEL)" +
                    " / 치매증상(DEMENTIA_SYMPTOM) / 동거인 여부(INMATE_STATE) / 어르신 필요 서비스(SERVICE) / 성별(GENDER)" +
                    " / 어르신 필요 서비스 - 식사보조(SERVICE_MEAL) / 어르신 필요 서비스 - 배변보조(SERVICE_TOILET)" +
                    " / 어르신 필요 서비스 - 이동보조(SERVICE_MOBILITY) / 어르신 필요 서비스 - 일상생활(SERVICE_DAILY)" +
                    "ex. 근무종류,치매증상 2개에 대한 데이터가 필요한 경우," +
                    " body 에 \"careTopEnumList\":[\"WORK_TYPE\", \"DEMENTIA_SYMPTOM\"]")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.SignInDto.class)))
    })
    public ResponseEntity<?> getRequestCodeList(@RequestBody CareRequestDto.GetRequestCodeList dto) {
        return ResponseEntity.ok().body(careService.getRequestCodeList(dto));
    }

}
