package com.balybus.galaxy.domain.tblImg;

import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.file.FileUploadUtils;
import lombok.RequiredArgsConstructor;
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
    public List<Long> uploadImg(MultipartFile[] photoFiles) {
        List<Long> imgSeq = new ArrayList<>();
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
