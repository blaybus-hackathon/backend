package com.balybus.galaxy.Center.serviceimpl.Service;

import com.balybus.galaxy.Center.domain.TblSignUpCenter;
import com.balybus.galaxy.Center.dto.CenterDto;
import com.balybus.galaxy.Center.repository.CenterRepository;
import com.balybus.galaxy.Center.serviceimpl.CenterService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CenterServiceImpl implements CenterService {

    private final CenterRepository centerRepository;

    @Override
    @Transactional
    public CenterDto registerCenter(CenterDto centerDto) {
        TblSignUpCenter center = centerRepository.save(centerDto.toEntity());
        return CenterDto.fromEntity(center);
    }
}