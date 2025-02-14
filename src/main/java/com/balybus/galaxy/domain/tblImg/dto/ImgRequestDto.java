package com.balybus.galaxy.domain.tblImg.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
public class ImgRequestDto {
    @Data
    public static class UploadImg{
        private MultipartFile[] photoFiles; // 첨부파일
    }
}
