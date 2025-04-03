package com.balybus.galaxy.address.serviceImpl;

import com.balybus.galaxy.address.domain.TblAddressFirst;

public interface TblAddressFirstService {
    TblAddressFirst validationCheck(Long afSeq); // 광역시.도 구분자 유효성 확인
}
