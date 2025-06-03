package com.balybus.galaxy.global.utils.file;

import com.balybus.galaxy.global.domain.tblImg.TblImg;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    TblImg uploadOneImgFile(MultipartFile file);    // 파일 이미지 하나 등록
    void deleteFile(Long fileSeq);                  // 등록된 이미지 삭제
    String getOneImgUrl(Long fileSeq);              // 등록된 이미지 조회
}
