package com.balybus.galaxy.global.common;

import com.balybus.galaxy.domain.tblImg.dto.ImgRequestDto;
import com.balybus.galaxy.domain.tblImg.dto.ImgResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface CommonService {
    /** 이미지 파일 저장 (요양보호사, 관리자, 어르신) */
    ImgResponseDto.uploadUserImg uploadUserImg(UserDetails userDetails, ImgRequestDto.uploadUserImg dto);
}
