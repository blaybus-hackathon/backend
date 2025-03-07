package com.balybus.galaxy.global.utils.file.service;

import com.balybus.galaxy.domain.tblImg.TblImg;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    TblImg uploadOneImgFile(MultipartFile file);
    void deleteFile(Long fileSeq);
}
