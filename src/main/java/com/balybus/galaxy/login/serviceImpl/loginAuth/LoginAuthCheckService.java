package com.balybus.galaxy.login.serviceImpl.loginAuth;

import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;

public interface LoginAuthCheckService {
    TblCenterManager checkManager(String userEmail); // 관리자 권한 확인
}
