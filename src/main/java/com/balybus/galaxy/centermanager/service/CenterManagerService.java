package com.balybus.galaxy.centermanager.service;

import com.balybus.galaxy.centermanager.dto.CenterManagerRequestDto;
import com.balybus.galaxy.centermanager.dto.CenterManagerResponseDto;
import com.balybus.galaxy.domain.tblCenter.TblCenter;
import com.balybus.galaxy.domain.tblCenter.TblCenterRepository;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.member.domain.TblUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CenterManagerService {
    private final TblCenterManagerRepository centerManagerRepository;
    private final TblCenterRepository centerRepository;

    public CenterManagerService(TblCenterManagerRepository centerManagerRepository, TblCenterRepository centerRepository) {
        this.centerManagerRepository = centerManagerRepository;
        this.centerRepository = centerRepository;
    }

    @Transactional
    public CenterManagerResponseDto updateCenterManager(Long id, CenterManagerRequestDto requestDto) {
        TblCenterManager centerManager = centerManagerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("센터 관리자를 찾을 수 없습니다. ID: " + id));

        if (requestDto.getCenterSeq() != null) {
            TblCenter center = centerRepository.findById(requestDto.getCenterSeq())
                    .orElseThrow(() -> new IllegalArgumentException("센터를 찾을 수 없습니다. CenterSeq: " + requestDto.getCenterSeq()));
            centerManager = TblCenterManager.builder()
                    .id(centerManager.getId())
                    .member(centerManager.getMember())
                    .center(center)
                    .img(centerManager.getImg())
                    .cmPosition(centerManager.getCmPosition())
                    .cmName(centerManager.getCmName())
                    .build();
        }
        if (requestDto.getPosition() != null) {
            centerManager = TblCenterManager.builder()
                    .id(centerManager.getId())
                    .member(centerManager.getMember())
                    .center(centerManager.getCenter())
                    .img(centerManager.getImg())
                    .cmPosition(requestDto.getPosition())
                    .cmName(centerManager.getCmName())
                    .build();
        }
        if (requestDto.getName() != null) {
            centerManager = TblCenterManager.builder()
                    .id(centerManager.getId())
                    .member(centerManager.getMember())
                    .center(centerManager.getCenter())
                    .img(centerManager.getImg())
                    .cmPosition(centerManager.getCmPosition())
                    .cmName(requestDto.getName())
                    .build();
        }
        // 유저 정보 업데이트 (TblUser의 email과 password 활용)
        TblUser member = centerManager.getMember();
        if (member == null) {
            throw new IllegalStateException("센터 관리자에 연결된 회원 정보가 없습니다. ID: " + id);
        }

        if (requestDto.getEmail() != null) {
            member = TblUser.builder()
                    .id(member.getId())
                    .email(requestDto.getEmail())
                    .password(member.getPassword())
                    .refreshToken(member.getRefreshToken())
                    .userAuth(member.getUserAuth())
                    .build();
        }
        if (requestDto.getPassword() != null) {
            member = TblUser.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .password(requestDto.getPassword())
                    .refreshToken(member.getRefreshToken())
                    .userAuth(member.getUserAuth())
                    .build();
        }

        TblCenterManager updatedCenterManager = centerManagerRepository.save(centerManager);
        return CenterManagerResponseDto.builder()
                .centerSeq(updatedCenterManager.getMember().getId())
                .build();
    }
}
