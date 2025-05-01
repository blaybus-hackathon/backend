package com.balybus.galaxy.centerManager.service;

import com.balybus.galaxy.centerManager.dto.CmRequestDto;
import com.balybus.galaxy.centerManager.dto.CmResponseDto;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.global.common.CommonServiceImpl;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.file.service.FileService;
import com.balybus.galaxy.login.serviceImpl.loginAuth.LoginAuthCheckServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CmServiceImpl implements CmService {
    private final LoginAuthCheckServiceImpl loginAuthCheckService;
    private final CommonServiceImpl commonService;
    private final FileService fileService;

    private final TblCenterManagerRepository centerManagerRepository;
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
            commonService.uploadImage(centerManagerRepository, centerManager.getId(), dto.getPhotoFile());
        }

        //3. 직책 데이터 변경
        centerManager.updatePosition(dto.getCmPosition());

        return CmResponseDto.UpdateManager.builder().cmSeq(centerManager.getId()).build();
    }

    @Override
    public CmResponseDto.UpdateCenter updateCenter(String userEmail, CmRequestDto.UpdateCenter dto) {
        return null;
    }

}
