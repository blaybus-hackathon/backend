package com.balybus.galaxy.centerManager.service;

import com.balybus.galaxy.centerManager.dto.CmResponseDto;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.utils.file.service.FileService;
import com.balybus.galaxy.login.serviceImpl.loginAuth.LoginAuthCheckServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CmServiceImpl implements CmService {
    private final LoginAuthCheckServiceImpl loginAuthCheckService;
    private final FileService fileService;
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
    public CmResponseDto.UpdateManager updateManager(String userEmail) {
        return null;
    }

    @Override
    public CmResponseDto.UpdateCenter updateCenter(String userEmail) {
        return null;
    }
}
