package com.balybus.galaxy.global.utils.address.service;

import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;

public interface TblAddressFirstService {
    TblAddressFirst validationCheck(Long afSeq); // 광역시.도 구분자 유효성 확인
}
