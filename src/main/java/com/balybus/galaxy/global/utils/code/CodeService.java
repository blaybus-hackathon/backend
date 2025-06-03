package com.balybus.galaxy.global.utils.code;

import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;
import com.balybus.galaxy.global.domain.tblAddressSecond.TblAddressSecond;
import com.balybus.galaxy.global.domain.tblAddressThird.TblAddressThird;

import java.time.LocalDate;

public interface CodeService {
    int calculateAge(LocalDate birthDate); //만 나이 계산기
    String fullAddressString(TblAddressFirst first, TblAddressSecond second, TblAddressThird third); // 주소 반환
}
