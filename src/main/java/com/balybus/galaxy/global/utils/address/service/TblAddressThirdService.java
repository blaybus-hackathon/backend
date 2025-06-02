package com.balybus.galaxy.global.utils.address.service;

import com.balybus.galaxy.global.domain.tblAddressThird.TblAddressThird;

public interface TblAddressThirdService {
    TblAddressThird validationCheck(Long secondSeq, Long thirdSeq); // 읍.면.동 구분자 유효성 확인
}
