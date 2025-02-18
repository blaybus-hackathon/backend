package com.balybus.galaxy.center.serviceimpl;

import com.balybus.galaxy.center.DTO.*;
import com.balybus.galaxy.center.domain.TblSingUpCenter;
import com.balybus.galaxy.center.repository.TblSingUpCenterRepository; // Repository 주입
import com.balybus.galaxy.domain.tblCenter.TblCenter;
import com.balybus.galaxy.domain.tblCenter.TblCenterRepository;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.global.exception.BadRequestException;

import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.member.repository.MemberRepository;
import com.balybus.galaxy.patient.domain.TblPatient;
import com.balybus.galaxy.patient.domain.TblPatientLog;
import com.balybus.galaxy.patient.repository.TblPatientLogRepository;
import com.balybus.galaxy.patient.repository.TblPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.balybus.galaxy.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class TblCenterServiceImpl {

    private final TblSingUpCenterRepository centerRepository; // Repository 주입
    private final MemberRepository memberRepository;
    private final TblCenterManagerRepository tblCenterManagerRepository;
    private final TblCenterRepository tblCenterRepository;
    private final TblPatientRepository tblPatientRepository;
    private final TblPatientLogRepository tblPatientLogRepository;

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

    public TblCenterManaegerUpdateResponseDTO updateCenterManager(TblCenterManagerUpdateDTO centerManagerUpdateDTO , UserDetails userDetails) {
        TblUser tblUser = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblCenterManager tblCenterManager = tblCenterManagerRepository.findByMember(tblUser)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MANAGER));

        TblCenter tblCenter = tblCenterRepository.findById(tblCenterManager.getCenter().getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_CENTER));

        tblCenter.updateCenterName(centerManagerUpdateDTO.getCenterName());
        tblCenterManager.updateCenterManagerName(centerManagerUpdateDTO.getName());
        tblCenterManager.updateCmPosition(centerManagerUpdateDTO.getPosition());

        return TblCenterManaegerUpdateResponseDTO.builder()
                .cmName(tblCenterManager.getCmName())
                .cmEmail(tblUser.getEmail())
                .build();
    }

    public List<TblPatientResponseDTO.PatientInfo> getPatient(UserDetails userDetails) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblCenterManager tblCenterManager = tblCenterManagerRepository.findByMember(tblUser)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        List<TblPatient> patientList = tblPatientRepository.findAllByManager(tblCenterManager);
        List<TblPatient> patientNotMatching = new ArrayList<>();
        for (TblPatient tblPatient : patientList) {
            if (tblPatient.getIsMatching() == 0) {
                patientNotMatching.add(tblPatient);
            }
        }
        List<TblPatientResponseDTO.PatientInfo> patientResponseDTOList = new ArrayList<>();
        for (TblPatient patient : patientNotMatching) {
            TblPatientLog tblPatientLog = tblPatientLogRepository.findByPatient(patient);
            patientResponseDTOList.add(
                    TblPatientResponseDTO.PatientInfo.builder()
                            .name(tblPatientLog.getName())
                            .gender(tblPatientLog.getGender())
                            .age(tblPatientLog.getBirthDate())
                            .workType(tblPatientLog.getWorkType())
                            .addressFirst(tblPatientLog.getTblAddressFirst().getName())
                            .addressSecond(tblPatientLog.getTblAddressSecond().getName())
                            .addressThree(tblPatientLog.getTblAddressThird().getName())
                            .careLevel(tblPatientLog.getCareLevel())
                            .build()
            );
        }
        return patientResponseDTOList;
    }
}
