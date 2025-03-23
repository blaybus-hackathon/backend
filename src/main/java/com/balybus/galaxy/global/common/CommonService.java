package com.balybus.galaxy.global.common;

import com.balybus.galaxy.domain.tblImg.dto.ImgRequestDto;
import com.balybus.galaxy.domain.tblImg.dto.ImgResponseDto;
import com.balybus.galaxy.login.domain.type.RoleType;
import org.springframework.security.core.userdetails.UserDetails;

public interface CommonService {
    ImgResponseDto.UploadUserImg uploadUserImg(UserDetails userDetails, RoleType roleType, ImgRequestDto.uploadUserImg dto);
}
