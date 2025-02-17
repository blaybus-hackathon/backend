package com.balybus.galaxy.domain.tblImg.dto;

import com.balybus.galaxy.login.domain.type.RoleType;
import lombok.Builder;
import lombok.Getter;

public class ImgResponseDto {
    @Getter
    @Builder
    public static class uploadUserImg{
        private Long imgSeq;            // 이미지 구분자
        private Long seq;               // 유저 구분자
        private RoleType imgOwnerAuth;  // 유저 권한
    }
}
