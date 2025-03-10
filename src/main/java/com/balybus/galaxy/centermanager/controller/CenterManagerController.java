package com.balybus.galaxy.centermanager.controller;

import com.balybus.galaxy.centermanager.dto.CenterManagerRequestDto;
import com.balybus.galaxy.centermanager.dto.CenterManagerResponseDto;
import com.balybus.galaxy.centermanager.service.CenterManagerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/centers/manager")
public class CenterManagerController {

    private final CenterManagerService centerManagerService;

    public CenterManagerController(CenterManagerService centerManagerService) {
        this.centerManagerService = centerManagerService;
    }

    /**
     * 센터 관리자가 자신의 정보를 수정하는 API
     * @param id 센터 관리자 ID
     * @param requestDto 수정할 관리자 정보
     * @return 수정된 관리자 정보
     */
    @PutMapping("/update/{id}")
    public CenterManagerResponseDto updateCenterManager(
            @PathVariable Long id,
            @RequestBody CenterManagerRequestDto requestDto) {
        return centerManagerService.updateCenterManager(id, requestDto);
    }
}