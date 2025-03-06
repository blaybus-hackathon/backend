package com.balybus.galaxy.domain.tblImg.dto;

import com.balybus.galaxy.login.domain.type.RoleType;
import lombok.Builder;
import lombok.Getter;

public class ImgResponseDto {
    @Getter
    @Builder
    public static class UploadLocalUserImg{
        private Long imgSeq;            // 이미지 구분자
    }
    @Getter
    @Builder
    public static class UploadDevUserImg{
        private Long imgSeq;            // 이미지 구분자
        private String preSignedUrl;  // PreSigned URL
    }
    @Getter
    @Builder
    public static class UploadUserImg{
        private Long imgSeq;            // 이미지 구분자
        private String preSignedUrl;  // PreSigned URL
    }
}
