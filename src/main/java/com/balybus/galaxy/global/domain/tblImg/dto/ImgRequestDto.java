package com.balybus.galaxy.global.domain.tblImg.dto;

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
        private MultipartFile photoFile;        //첨부파일

        private Long helperSeq;                 //요양보호사 구분자(요양보호사:tblHelper)
        private Long managerSeq;                //관리자 구분자(관리자:tblCenterManager)
        private Long patientSeq;                //어르신 구분자(어르신:tblPatient)
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
