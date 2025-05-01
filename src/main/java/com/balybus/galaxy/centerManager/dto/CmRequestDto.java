package com.balybus.galaxy.centerManager.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class CmRequestDto {
    @Data
    public static class UpdateManager{
        private Long cmSeq;                     //관리자 구분자

        private boolean imgChangeYn;            //이미지 변경 여부
        private MultipartFile photoFile;        //이미지 파일
        private String cmPosition;              //직책
    }
}
