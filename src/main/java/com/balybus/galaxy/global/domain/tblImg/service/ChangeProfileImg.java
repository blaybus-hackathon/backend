package com.balybus.galaxy.global.domain.tblImg.service;

import com.balybus.galaxy.global.domain.tblImg.TblImg;

public interface ChangeProfileImg {
    /* =========================================
     * (요양보호사, 관리자, 어르신) 이미지 파일 저장관련
     * ========================================= */
    TblImg getImg();
    void updateImg(TblImg img);
}
