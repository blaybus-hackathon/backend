package com.balybus.galaxy.center.controller;

import com.balybus.galaxy.center.dto.CenterRequestDto;
import com.balybus.galaxy.center.dto.CenterResponseDto;
import com.balybus.galaxy.center.service.CenterListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/centers")
public class CenterListController {

    private final CenterListService centerListService;

    public CenterListController(CenterListService centerListService) {
        this.centerListService = centerListService;
    }

    @GetMapping("/search")
    public List<CenterResponseDto> searchCenters(
            @RequestParam String searchType,
            @RequestParam String keyword
    ) {
        return centerListService.getCenters(searchType, keyword);
    }

    @PostMapping("/create")
    public CenterResponseDto createCenter(@RequestBody CenterRequestDto requestDto) {
        return centerListService.createCenter(requestDto);
    }

    @PutMapping("/update/{id}")
    public CenterResponseDto updateCenter(
            @PathVariable Long id,
            @RequestBody CenterRequestDto requestDto
    ) {
        return centerListService.updateCenter(id, requestDto);
    }
}

