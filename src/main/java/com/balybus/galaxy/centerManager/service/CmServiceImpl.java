package com.balybus.galaxy.centerManager.service;

import com.balybus.galaxy.centerManager.dto.CmRequestDto;
import com.balybus.galaxy.centerManager.dto.CmResponseDto;
import com.balybus.galaxy.global.domain.tblCenter.TblCenter;
import com.balybus.galaxy.global.domain.tblCenter.TblCenterRepository;
import com.balybus.galaxy.global.domain.tblCenter.dto.CenterRequestDto;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.global.domain.tblImg.service.TblImgServiceImpl;
import com.balybus.galaxy.global.domain.tblMatching.TblMatchingRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.file.service.FileService;
import com.balybus.galaxy.login.classic.service.loginAuth.LoginAuthCheckServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CmServiceImpl implements CmService {
    private final LoginAuthCheckServiceImpl loginAuthCheckService;
    private final TblImgServiceImpl imgService;
    private final FileService fileService;

    private final TblCenterManagerRepository centerManagerRepository;
    private final TblCenterRepository centerRepository;
    private final TblMatchingRepository matchingRepository;

    @Override
    public CmResponseDto.GetOneManager getOneManager(String userEmail) {
        //1. 로그인 정보를 기준으로 관리자 정보를 탐색한다. (관리자 정보가 없을 경우, 에러 메시지 반환)
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        //2. 관리자 정보가 있는 경우, 정보를 dto 에 담아서 반환한다.
        return CmResponseDto.GetOneManager.builder()
                .centerSeq(centerManager.getCenter().getId())
                .centerName(centerManager.getCenter().getCenterName())
                .cmSeq(centerManager.getId())
                .cmName(centerManager.getCmName())
                .cmPosition(centerManager.getCmPosition())
                .imgSeq(centerManager.getImg() == null ? null : centerManager.getImg().getId())
                .imgAddress(centerManager.getImg() == null ? null : fileService.getOneImgUrl(centerManager.getImg().getId()))
                .build();
    }

    @Override
    @Transactional
    public CmResponseDto.UpdateManager updateManager(String userEmail, CmRequestDto.UpdateManager dto) {
        //1. 관리자 정보 유효성 검사
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);
        if(!centerManager.getId().equals(dto.getCmSeq())) throw new BadRequestException(ExceptionCode.UNAUTHORIZED_UPDATE);

        //2. 이미지 변경 여부가 true 인 경우, 이미지 데이터 변경
        if(dto.isImgChangeYn()){
            imgService.uploadImage(centerManagerRepository, centerManager.getId(), dto.getPhotoFile());
        }

        //3. 직책 데이터 변경
        centerManager.updatePosition(dto.getCmPosition());

        return CmResponseDto.UpdateManager.builder().cmSeq(centerManager.getId()).build();
    }

    @Override
    public CmResponseDto.GetOneCenter getOneCenter(String userEmail, Long centerSeq) {
        //1. 관리자 정보 유효성 검사
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);
        if(!centerManager.getCenter().getId().equals(centerSeq)) throw new BadRequestException(ExceptionCode.UNAUTHORIZED);

        //2. 센터 정보 조회
        Optional<TblCenter> centerOpt = centerRepository.findById(centerSeq);
        if(centerOpt.isEmpty()) throw new BadRequestException(ExceptionCode.CENTER_NOT_FOUND);

        TblCenter center = centerOpt.get();
        return CmResponseDto.GetOneCenter.builder()
                .centerSeq(center.getId())
                .name(center.getCenterName())
                .tel(center.getCenterTel())
                .carYn(center.isCenterCarYn())
                .address(center.getCenterAddress())
                .grade(center.getCenterGrade())
                .openDate(center.getCenterOpenDate())
                .introduce(center.getCenterIntroduce())
                .build();
    }

    @Override
    @Transactional
    public CmResponseDto.UpdateCenter updateCenter(String userEmail, CenterRequestDto.UpdateCenter dto) {
        //1. 관리자 정보 유효성 검사
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);
        if(!centerManager.getCenter().getId().equals(dto.getCenterSeq())) throw new BadRequestException(ExceptionCode.UNAUTHORIZED_UPDATE);

        //2. 센터 정보 조회
        Optional<TblCenter> centerOpt = centerRepository.findById(centerManager.getCenter().getId());
        if(centerOpt.isEmpty()) throw new BadRequestException(ExceptionCode.CENTER_NOT_FOUND);

        //3. 기존에 등록된 센터 주소가 있는지 확인하기
        Optional<TblCenter> centerAddressOpt = centerRepository.findByCenterAddress(dto.getAddress());
        if(centerAddressOpt.isPresent()){
            if(!centerAddressOpt.get().getId().equals(centerOpt.get().getId()))
                throw new BadRequestException(ExceptionCode.CENTER_EXIST);
        }

        //4. 센터 정보 수정
        TblCenter center = centerOpt.get();
        center.updateCenter(dto);

        return CmResponseDto.UpdateCenter.builder().centerSeq(center.getId()).build();
    }

    @Override
    public CmResponseDto.GetStatisticsDashboard getStatisticsDashboard(String userEmail) {
        //1. 관리자 정보 유효성 검사
        TblCenterManager centerManager = loginAuthCheckService.checkManager(userEmail);

        //2. 해당 관리자가 관리하고 있는 공고 조회
        List<Object[]> getStatistics = matchingRepository.findByCmSeqToGetStatisticsDashboard(centerManager.getId());
        return getStatistics.isEmpty() ?
                new CmResponseDto.GetStatisticsDashboard()
                : new CmResponseDto.GetStatisticsDashboard(getStatistics.get(0));
    }

}
