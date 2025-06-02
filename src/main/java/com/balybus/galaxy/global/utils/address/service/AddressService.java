package com.balybus.galaxy.global.utils.address.service;

import com.balybus.galaxy.global.utils.address.dto.response.TblAddressFirstResponse;
import com.balybus.galaxy.global.utils.address.dto.response.TblAddressSecondResponse;
import com.balybus.galaxy.global.utils.address.dto.response.TblAddressThirdResponse;

import java.util.List;

public interface AddressService {
    List<TblAddressFirstResponse> getFirstAddress();
    List<TblAddressThirdResponse> getThirdAddressBySecondId(Long asSeq);
    List<TblAddressSecondResponse> getAddressSecondByFirstId(Long afSeq);
}
