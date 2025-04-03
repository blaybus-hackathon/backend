package com.balybus.galaxy.address.serviceImpl.service;

import com.balybus.galaxy.address.domain.TblAddressSecond;
import com.balybus.galaxy.address.repository.TblAddressSecondRepository;
import com.balybus.galaxy.address.serviceImpl.TblAddressSecondService;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TblAddressSecondServiceImpl implements TblAddressSecondService {
    private final TblAddressSecondRepository addressSecondRepository;

    /**
     * 시.군.구 구분자 유효성 확인
     * @param firstSeq Long:광역시.도 구분자
     * @param secondSeq Long:시.군.구 구분자
     * @return TblAddressSecond: 시.군.구 Entity
     */
    @Override
    public TblAddressSecond validationCheck(Long firstSeq, Long secondSeq) {
        Optional<TblAddressSecond> secondOpt = addressSecondRepository.findByAddressFirstIdAndId(firstSeq, secondSeq);
        if(secondOpt.isEmpty()) throw new BadRequestException(ExceptionCode.INVALID_ADDRESS);
        return secondOpt.get();
    }
}
