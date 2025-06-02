package com.balybus.galaxy.global.utils.address.service.serviceImpl;

import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;
import com.balybus.galaxy.global.utils.address.repository.TblAddressFirstRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.address.service.TblAddressFirstService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TblAddressFirstServiceImpl implements TblAddressFirstService {
    private final TblAddressFirstRepository addressFirstRepository;

    /**
     * 광역시.도 구분자 유효성 확인
     * @param afSeq Long:광역시.도 구분자
     * @return TblAddressFirst:광역시.도 Entity
     */
    @Override
    public TblAddressFirst validationCheck(Long afSeq) {
        Optional<TblAddressFirst> firstOpt = addressFirstRepository.findById(afSeq);
        if(firstOpt.isEmpty()) throw new BadRequestException(ExceptionCode.INVALID_ADDRESS);
        return firstOpt.get();
    }
}
