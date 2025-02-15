package com.balybus.galaxy.domain.tblImg;

import com.balybus.galaxy.domain.tblImg.dto.ImgRequestDto;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TblImgServiceImpl {
    private final FileUploadUtils fileUploadUtils;

    @Transactional
    public List<Long> uploadImg(UserDetails userDetails, ImgRequestDto.UploadImg dto) {
        List<Long> imgSeq = new ArrayList<>();
        MultipartFile[] photoFiles = dto.getPhotoFiles();
        if (photoFiles != null && photoFiles.length > 0) {
            try {
                imgSeq = fileUploadUtils.uploadFiles(photoFiles);
            } catch (BadRequestException e) {
                throw e;
            }  catch (Exception e) {
                throw new BadRequestException(ExceptionCode.INTERNAL_SEVER_ERROR);
            }
        }

        return imgSeq;
    }

}
