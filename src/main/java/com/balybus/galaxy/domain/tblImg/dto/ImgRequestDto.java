package com.balybus.galaxy.domain.tblImg.dto;

import com.balybus.galaxy.login.domain.type.RoleType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImgRequestDto {
    @Data
    public static class UploadImg{
        private MultipartFile[] photoFiles; // 첨부파일
    }
    @Data
    public static class uploadUserImg{
        private MultipartFile[] photoFiles;     //첨부파일
        private String fileName;                //파일명

        private Long helperSeq;                 //요양보호사 구분자(요양보호사:tblHelper)
        private Long managerSeq;                //관리자 구분자(관리자:tblCenterManager)
        private Long patientSeq;                //어르신 구분자(어르신:tblPatient)

        private RoleType imgOwnerAuth;          //이미지 주체자 권한(요양보호사:MEMBER, 관리자:MANAGER, 어르신:PATIENT)
    }

    @Data
    public static class PreSignedUploadInitiateRequest {
        private String originalFileName;
        private String fileType;
        private Long fileSize;
    }

    @Data
    public static class PreSignedUrlCreateRequest {
        private String fileName;
    }

    @Data
    public static class FinishUploadRequest {
        String uploadId;
        List<Part> parts;

        @Data
        public class Part {
            Integer partNumber;
            String eTag;
        }
    }

    @Data
    public static class PreSignedUrlAbortRequest {
        private String uploadId;
    }
}
