package com.balybus.galaxy.login.classic.service.loginAuth;

import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.login.classic.domain.type.RoleType;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.member.repository.MemberRepository;
import com.balybus.galaxy.patient.domain.tblPatient.TblPatient;
import com.balybus.galaxy.patient.domain.tblPatient.TblPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginAuthCheckServiceImpl implements LoginAuthCheckService{
    private final MemberRepository memberRepository;
    private final TblCenterManagerRepository centerManagerRepository;
    private final TblPatientRepository patientRepository;
    @Override
    public TblCenterManager checkManager(String userEmail) {
        //1. 로그인 테이블 조회
        Optional<TblUser> userOpt = memberRepository.findByEmail(userEmail); // 토큰 이메일로 정보 조회
        if(userOpt.isEmpty()) throw new BadRequestException(ExceptionCode.DO_NOT_LOGIN);
        TblUser userEntity = userOpt.get();

        //2. 관리자 테이블 조회
        Optional<TblCenterManager> centerManagerOpt = centerManagerRepository.findByMember_Id(userEntity.getId());
        if(!userEntity.getUserAuth().equals(RoleType.MANAGER)
                || centerManagerOpt.isEmpty()) throw new BadRequestException(ExceptionCode.NOT_FOUND_MANAGER);
        return centerManagerOpt.get();
    }

    /**
     * 관리자 사용자의 어르신 정보 접근 권한 확인
     * @param patientSeq Long: 어르신(TblPatient) 구분자
     * @param managerSeq Long: 센터 관리자(TblCenterManager) 구분자
     * @return TblPatient
     */
    @Override
    public TblPatient checkPatientManagerAuth(Long patientSeq, Long managerSeq){
        Optional<TblPatient> patientOpt = patientRepository.findById(patientSeq);
        if(patientOpt.isEmpty()) throw new BadRequestException(ExceptionCode.NOT_FOUND_PATIENT);
        TblPatient patient = patientOpt.get();
        if(!patient.getManager().getId().equals(managerSeq))
            throw new BadRequestException(ExceptionCode.UNAUTHORIZED_UPDATE);

        return patient;
    }
}
