package com.balybus.galaxy.global.domain.tblImg.service;

import com.balybus.galaxy.global.domain.tblImg.dto.ImgRequestDto;
import com.balybus.galaxy.global.domain.tblImg.dto.ImgResponseDto;
import com.balybus.galaxy.global.utils.file.ChangeProfileImg;
import com.balybus.galaxy.login.classic.domain.type.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface TblImgService {
    ImgResponseDto.UploadUserImg uploadUserImg(UserDetails userDetails, RoleType roleType, ImgRequestDto.uploadUserImg dto); //권한 분기 프로필 이미지 변경 및 업로드 API
    ImgResponseDto.UploadUserImg uploadImage(JpaRepository<? extends ChangeProfileImg, Long> repository, Long seq, MultipartFile photoFile); //이미지 파일 저장
}
