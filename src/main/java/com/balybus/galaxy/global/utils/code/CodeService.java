package com.balybus.galaxy.global.utils.code;

import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;
import com.balybus.galaxy.global.domain.tblAddressSecond.TblAddressSecond;
import com.balybus.galaxy.global.domain.tblAddressThird.TblAddressThird;
import com.balybus.galaxy.global.domain.tblImg.dto.ImgRequestDto;
import com.balybus.galaxy.global.domain.tblImg.dto.ImgResponseDto;
import com.balybus.galaxy.login.classic.domain.type.RoleType;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;

public interface CodeService {
    ImgResponseDto.UploadUserImg uploadUserImg(UserDetails userDetails, RoleType roleType, ImgRequestDto.uploadUserImg dto);
    int calculateAge(LocalDate birthDate); //만 나이 계산기
    String fullAddressString(TblAddressFirst first, TblAddressSecond second, TblAddressThird third); // 주소 반환
}
