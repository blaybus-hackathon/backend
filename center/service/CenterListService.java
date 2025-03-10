package com.balybus.galaxy.center.service;

import com.balybus.galaxy.center.dto.CenterRequestDto;
import com.balybus.galaxy.center.dto.CenterResponseDto;
import com.balybus.galaxy.center.entity.CenterEntity;
import com.balybus.galaxy.center.repository.CenterListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CenterListService {
    private final CenterListRepository centerListRepository;

    public CenterListService(CenterListRepository centerListRepository) {
        this.centerListRepository = centerListRepository;
    }

    public List<CenterResponseDto> getCenters(String searchType, String keyword) {
        List<CenterEntity> centers = centerListRepository.searchCenters(searchType, keyword);
        return centers.stream().map(CenterResponseDto::new).collect(Collectors.toList());
    }

    public CenterResponseDto createCenter(CenterRequestDto requestDto) {
        CenterEntity center = new CenterEntity();
        center.setIssueCode(requestDto.getIssueCode());
        center.setName(requestDto.getName());
        center.setAddress(requestDto.getAddress());
        center.setDescription(requestDto.getDescription());

        CenterEntity savedCenter = centerListRepository.save(center);
        return new CenterResponseDto(savedCenter);
    }

    @Transactional
    public CenterResponseDto updateCenter(Long id, CenterRequestDto requestDto) {
        CenterEntity center = centerListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("센터를 찾을 수 없습니다. ID: " + id));

        if (requestDto.getIssueCode() != null) {
            center.setIssueCode(requestDto.getIssueCode());
        }
        if (requestDto.getName() != null) {
            center.setName(requestDto.getName());
        }
        if (requestDto.getAddress() != null) {
            center.setAddress(requestDto.getAddress());
        }
        if (requestDto.getDescription() != null) {
            center.setDescription(requestDto.getDescription());
        }

        return new CenterResponseDto(center);
    }
}