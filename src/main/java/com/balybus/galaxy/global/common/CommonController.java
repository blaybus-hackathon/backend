package com.balybus.galaxy.global.common;

import com.balybus.galaxy.domain.tblCare.dto.CareRequestDto;
import com.balybus.galaxy.domain.tblCare.service.TblCareServiceImpl;
import com.balybus.galaxy.domain.tblImg.dto.ImgRequestDto;
import com.balybus.galaxy.global.exception.ErrorResponse;
import com.balybus.galaxy.login.domain.type.RoleType;
import com.balybus.galaxy.member.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/cmn")
public class CommonController {

    private final CommonServiceImpl commonService;
    private final TblCareServiceImpl careService;

    @PostMapping("/upload-img/{roleType}")
    @Operation(summary = "프로필 이미지 변경 및 업로드 API",
            description = "주체(요양보호사, 관리자, 어르신)의 프로필 이미지를 업로드하고 서버에 저장하는 기능을 제공합니다. " +
                    "이미지 파일은 multipart/form-data로 전송해야 하며, 성공적으로 업로드되면 이미지 구분자(imgSeq)가 반환됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 이미지 변경 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.SignInDto.class))),
            @ApiResponse(responseCode = "4005", description = "이미지 등록 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5000", description = "예상하지 못한 서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "6000", description = "사용자정의에러코드:파일 업로드 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "6001", description = "사용자정의에러코드:삭제 파일을 찾지 못함",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "6002", description = "사용자정의에러코드:2개 이상의 파일이 전송됨",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "6003", description = "사용자정의에러코드:이미지를 등록하려는 주체 정보 조회 불가",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> uploadUserImg(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable RoleType roleType,
                                           ImgRequestDto.uploadUserImg dto) {
        return ResponseEntity.ok().body(commonService.uploadUserImg(userDetails, roleType, dto));
    }

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
