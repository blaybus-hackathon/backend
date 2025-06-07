package com.balybus.galaxy.login.classic.service.loginAuth;

import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.domain.tblPatient.TblPatient;

public interface LoginAuthCheckService {
    TblCenterManager checkManager(String userEmail); // 관리자 권한 확인
    TblPatient checkPatientManagerAuth(Long patientSeq, Long managerSeq); // 관리자 사용자의 어르신 정보 접근 권한 확인
}
