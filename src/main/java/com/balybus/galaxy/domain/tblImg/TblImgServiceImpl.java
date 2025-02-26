package com.balybus.galaxy.domain.tblImg;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.file.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TblImgServiceImpl {
    private final FileUploadUtils fileUploadUtils;
    private final AmazonS3Client amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

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

    /**
     * S3에 이미지 업로드 하기
     */
    public String uploadImageToS3(MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename(); // 고유한 파일 이름 생성

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        // S3에 파일 업로드 요청 생성
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, image.getInputStream(), metadata);

        // S3에 파일 업로드
        amazonS3.putObject(putObjectRequest);

        return getPublicUrl(fileName);
    }

    private String getPublicUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, amazonS3.getRegionName(), fileName);
    }

}
