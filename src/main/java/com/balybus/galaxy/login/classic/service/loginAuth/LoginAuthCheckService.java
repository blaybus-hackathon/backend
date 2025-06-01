package com.balybus.galaxy.login.classic.service.loginAuth;

import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;

public interface LoginAuthCheckService {
    TblCenterManager checkManager(String userEmail); // 관리자 권한 확인
}
