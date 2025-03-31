package com.balybus.galaxy.address.serviceImpl;

import com.balybus.galaxy.address.domain.TblAddressSecond;

public interface TblAddressSecondService {
    TblAddressSecond validationCheck(Long firstSeq, Long secondSeq); // 시.군.구 구분자 유효성 확인
}
