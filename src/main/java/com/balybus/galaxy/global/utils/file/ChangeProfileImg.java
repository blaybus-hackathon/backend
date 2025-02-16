package com.balybus.galaxy.global.utils.file;

import com.balybus.galaxy.domain.tblImg.TblImg;

public interface ChangeProfileImg {
    /* =========================================
     * (요양보호사, 관리자, 어르신) 이미지 파일 저장관련
     * ========================================= */
    TblImg getImg();
    void updateImg(TblImg img);
}
