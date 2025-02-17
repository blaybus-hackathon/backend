package com.balybus.galaxy.domain.tblImg.dto;

import com.balybus.galaxy.login.domain.type.RoleType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
public class ImgRequestDto {
    @Data
    public static class UploadImg{
        private MultipartFile[] photoFiles; // 첨부파일
    }
    @Data
    public static class uploadUserImg{
        private MultipartFile[] photoFiles;     // 첨부파일
        private Long seq;                       //유저 구분자(요양보호사:tblHelper, 관리자:tblCenterManager, 어르신:tblPatient)
        private RoleType imgOwnerAuth;          //이미지 주체자 권한(요양보호사:MEMBER, 관리자:MANAGER, 어르신:PATIENT)
    }
}
