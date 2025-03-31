package com.balybus.galaxy.global.common;

import com.balybus.galaxy.address.domain.TblAddressFirst;
import com.balybus.galaxy.address.domain.TblAddressSecond;
import com.balybus.galaxy.address.domain.TblAddressThird;
import com.balybus.galaxy.domain.tblImg.dto.ImgRequestDto;
import com.balybus.galaxy.domain.tblImg.dto.ImgResponseDto;
import com.balybus.galaxy.login.domain.type.RoleType;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;

public interface CommonService {
    ImgResponseDto.UploadUserImg uploadUserImg(UserDetails userDetails, RoleType roleType, ImgRequestDto.uploadUserImg dto);
    int calculateAge(LocalDate birthDate); //만 나이 계산기
    String fullAddressString(TblAddressFirst first, TblAddressSecond second, TblAddressThird third); // 주소 반환
}
