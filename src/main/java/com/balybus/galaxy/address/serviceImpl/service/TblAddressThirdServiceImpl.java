package com.balybus.galaxy.address.serviceImpl.service;

import com.balybus.galaxy.address.domain.TblAddressThird;
import com.balybus.galaxy.address.repository.TblAddressThirdRepository;
import com.balybus.galaxy.address.serviceImpl.TblAddressThirdService;
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
public class TblAddressThirdServiceImpl implements TblAddressThirdService {
    private final TblAddressThirdRepository addressThirdRepository;

    /**
     * 읍.면.동 구분자 유효성 확인
     * @param secondSeq Long:시.군.구 구분자
     * @param thirdSeq Long:읍.면.동 구분자
     * @return TblAddressThird:읍.면.동 Entity
     */
    @Override
    public TblAddressThird validationCheck(Long secondSeq, Long thirdSeq) {
        Optional<TblAddressThird> thirdOpt = addressThirdRepository.findByAddressSecond_IdAndId(secondSeq, thirdSeq);
        if(thirdOpt.isEmpty()) throw new BadRequestException(ExceptionCode.INVALID_ADDRESS);
        return thirdOpt.get();
    }
}
