package com.balybus.galaxy.global.utils.address.service;

import com.balybus.galaxy.global.domain.tblAddressSecond.TblAddressSecond;

public interface TblAddressSecondService {
    TblAddressSecond validationCheck(Long firstSeq, Long secondSeq); // 시.군.구 구분자 유효성 확인
}
