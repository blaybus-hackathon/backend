package com.balybus.galaxy.global.utils.address.service.serviceImpl;

import com.balybus.galaxy.global.utils.address.dto.response.TblAddressFirstResponse;
import com.balybus.galaxy.global.utils.address.dto.response.TblAddressSecondResponse;
import com.balybus.galaxy.global.utils.address.dto.response.TblAddressThirdResponse;
import com.balybus.galaxy.global.domain.tblAddressSecond.TblAddressSecond;
import com.balybus.galaxy.global.domain.tblAddressThird.TblAddressThird;
import com.balybus.galaxy.global.utils.address.repository.TblAddressFirstRepository;
import com.balybus.galaxy.global.utils.address.repository.TblAddressSecondRepository;
import com.balybus.galaxy.global.utils.address.repository.TblAddressThirdRepository;
import com.balybus.galaxy.global.utils.address.service.AddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final TblAddressFirstRepository tblAddressFirstRepository;
    private final TblAddressSecondRepository tblAddressSecondRepository;
    private final TblAddressThirdRepository tblAddressThirdRepository;

    @Override
    public List<TblAddressFirstResponse> getFirstAddress() {
        return tblAddressFirstRepository.findAll().stream()
                .map(TblAddressFirstResponse::new)  // DTO로 변환
                .collect(Collectors.toList());
    }

    @Override
    public List<TblAddressThirdResponse> getThirdAddressBySecondId(Long asSeq) {
        List<TblAddressThird> entities = tblAddressThirdRepository.findByAddressSecond_Id(asSeq);

        return entities.stream()
                .map(TblAddressThirdResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TblAddressSecondResponse> getAddressSecondByFirstId(Long afSeq) {
        List<TblAddressSecond> entities = tblAddressSecondRepository.findByAddressFirst_Id(afSeq);

        return entities.stream()
                .map(TblAddressSecondResponse::new)
                .collect(Collectors.toList());
    }

}
