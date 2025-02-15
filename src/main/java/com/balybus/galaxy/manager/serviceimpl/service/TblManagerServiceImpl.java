package com.balybus.galaxy.manager.serviceimpl.service;

import com.balybus.galaxy.manager.DTO.TblManagerResponseDTO;
import com.balybus.galaxy.manager.DTO.TblManagerSignUpDTO;
import com.balybus.galaxy.manager.DTO.TblManagerLoginDTO;
import com.balybus.galaxy.manager.domain.TblManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TblManagerServiceImpl {

    private final com.balybus.galaxy.manager.repository.TblManagerRepository managerRepository;

    // 관리자 회원가입
    public TblManagerResponseDTO registerManager(TblManagerSignUpDTO signUpDTO) {
        // 유저 구분자 중복 체크
        if (managerRepository.findByUserSeq(signUpDTO.getUserSeq()).isPresent()) {
            throw new RuntimeException("이미 존재하는 유저 구분자입니다.");
        }

        // 관리자 엔티티 저장
        TblManager manager = TblManager.builder()
                .userSeq(signUpDTO.getUserSeq())  // 유저 구분자
                .centerSeq(signUpDTO.getCenterSeq())  // 센터 구분자
                .position(signUpDTO.getPosition())  // 직책
                .name(signUpDTO.getName())  // 직원명
                .password(signUpDTO.getPassword())  // 비밀번호
                .build();

        TblManager savedManager = managerRepository.save(manager);

        // ResponseDTO 반환
        return TblManagerResponseDTO.builder()
                .id(savedManager.getId())  // 관리자 구분자
                .userSeq(savedManager.getUserSeq())  // 유저 구분자
                .centerSeq(savedManager.getCenterSeq())  // 센터 구분자
                .position(savedManager.getPosition())  // 직책
                .name(savedManager.getName())  // 직원명
                .createdAt(savedManager.getCreatedAt().toString())  // 생성일시
                .updatedAt(savedManager.getUpdatedAt().toString())  // 수정일시
                .build();
    }

    // 관리자 로그인
    public TblManagerResponseDTO loginManager(TblManagerLoginDTO loginDTO) {
        TblManager manager = managerRepository.findByUserSeq(loginDTO.getUserSeq())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 비밀번호 확인 로직
        if (!manager.getPassword().equals(loginDTO.getPassword())) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        return TblManagerResponseDTO.builder()
                .id(manager.getId())  // 관리자 구분자
                .userSeq(manager.getUserSeq())  // 유저 구분자
                .name(manager.getName())  // 직원명
                .position(manager.getPosition())  // 직책
                .createdAt(manager.getCreatedAt().toString())  // 생성일시
                .updatedAt(manager.getUpdatedAt().toString())  // 수정일시
                .build();
    }
}
