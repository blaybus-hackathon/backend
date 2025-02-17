package com.balybus.galaxy.center.serviceimpl;

import com.balybus.galaxy.center.DTO.TblCenterResponseDTO;
import com.balybus.galaxy.center.DTO.TblCenterSignUpDTO;
import com.balybus.galaxy.center.domain.TblSingUpCenter;
import com.balybus.galaxy.center.repository.TblSingUpCenterRepository; // Repository 주입
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TblCenterServiceImpl {

    private final TblSingUpCenterRepository centerRepository; // Repository 주입

    public TblCenterResponseDTO registerCenter(TblCenterSignUpDTO signUpDTO) {
        // 센터명 중복 체크
        if (centerRepository.findByCenterName(signUpDTO.getCenterName()).isPresent()) {
            throw new RuntimeException("이미 존재하는 센터명입니다.");
        }

        // TblSingUpCenter 엔티티 저장
        TblSingUpCenter center = TblSingUpCenter.builder()
                .imgSeq(signUpDTO.getImgSeq())
                .centerName(signUpDTO.getCenterName())
                .centerTel(signUpDTO.getCenterTel())
                .centerCarYn(signUpDTO.getCenterCarYn())
                .centerAddress(signUpDTO.getCenterAddress())
                .centerGrade(signUpDTO.getCenterGrade())
                .centerOpenDate(LocalDateTime.from(signUpDTO.getCenterOpenDate()))
                .centerIntroduce(signUpDTO.getCenterIntroduce())
                .build();

        // 센터 저장
        TblSingUpCenter savedCenter = centerRepository.save(center);

        // ResponseDTO 반환
        return TblCenterResponseDTO.builder()
                .id(savedCenter.getId())
                .imgSeq(savedCenter.getImgSeq())
                .centerName(savedCenter.getCenterName())
                .centerTel(savedCenter.getCenterTel())
                .centerCarYn(savedCenter.getCenterCarYn())
                .centerAddress(savedCenter.getCenterAddress())
                .centerGrade(savedCenter.getCenterGrade())
                .centerOpenDate(savedCenter.getCenterOpenDate())
                .centerIntroduce(savedCenter.getCenterIntroduce())
                .build();
    }
}
