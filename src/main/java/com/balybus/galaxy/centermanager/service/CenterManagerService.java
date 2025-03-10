package com.balybus.galaxy.centermanager.service;

import com.balybus.galaxy.centermanager.dto.CenterManagerRequestDto;
import com.balybus.galaxy.centermanager.dto.CenterManagerResponseDto;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManagerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CenterManagerService {
    private final TblCenterManagerRepository centerManagerRepository;

    public CenterManagerService(TblCenterManagerRepository centerManagerRepository) {
        this.centerManagerRepository = centerManagerRepository;
    }

    /**
     * 센터 관리자 정보 수정
     * @param id 센터 관리자 ID
     * @param requestDto 수정할 관리자 정보
     * @return 수정된 관리자 정보
     */
    @Transactional
    public CenterManagerResponseDto updateCenterManager(Long id, CenterManagerRequestDto requestDto) {
        // 기존 관리자 정보 찾기
        TblCenterManager centerManager = centerManagerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("센터 관리자를 찾을 수 없습니다. ID: " + id));

        // 수정할 값이 있는 경우에만 수정
        if (requestDto.getCenterSeq() != null) {
            centerManager.setCenterSeq(requestDto.getCenterSeq());
        }
        if (requestDto.getPosition() != null) {
            centerManager.setPosition(requestDto.getPosition());
        }
        if (requestDto.getName() != null) {
            centerManager.setName(requestDto.getName());
        }
        if (requestDto.getEmail() != null) {
            centerManager.setEmail(requestDto.getEmail());
        }
        if (requestDto.getPassword() != null) {
            centerManager.setPassword(requestDto.getPassword());
        }

        // 수정된 정보를 저장하고 반환
        TblCenterManager updatedCenterManager = centerManagerRepository.save(centerManager);
        return new CenterManagerResponseDto(updatedCenterManager);
    }
}