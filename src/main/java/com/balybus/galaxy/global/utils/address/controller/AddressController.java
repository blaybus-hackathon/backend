package com.balybus.galaxy.global.utils.address.controller;

import com.balybus.galaxy.global.utils.address.dto.response.TblAddressFirstResponse;
import com.balybus.galaxy.global.utils.address.dto.response.TblAddressSecondResponse;
import com.balybus.galaxy.global.utils.address.dto.response.TblAddressThirdResponse;
import com.balybus.galaxy.global.utils.address.service.serviceImpl.AddressServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class AddressController {

    private final AddressServiceImpl addressService;

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
        List<TblAddressFirstResponse> addressList = addressService.getFirstAddress();
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
        List<TblAddressSecondResponse> addressSeconds = addressService.getAddressSecondByFirstId(afSeq);
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
        List<TblAddressThirdResponse> thirdAddresses = addressService.getThirdAddressBySecondId(asSeq);
        return ResponseEntity.ok(thirdAddresses);
    }
}
