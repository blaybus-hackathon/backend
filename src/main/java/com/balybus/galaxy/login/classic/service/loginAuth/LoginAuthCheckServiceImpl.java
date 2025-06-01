package com.balybus.galaxy.login.classic.service.loginAuth;

import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.login.classic.domain.type.RoleType;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginAuthCheckServiceImpl implements LoginAuthCheckService{
    private final MemberRepository memberRepository;
    private final TblCenterManagerRepository centerManagerRepository;
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
}
