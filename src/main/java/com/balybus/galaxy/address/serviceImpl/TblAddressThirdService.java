package com.balybus.galaxy.address.serviceImpl;

import com.balybus.galaxy.address.domain.TblAddressThird;

public interface TblAddressThirdService {
    TblAddressThird validationCheck(Long secondSeq, Long thirdSeq); // 읍.면.동 구분자 유효성 확인
}
