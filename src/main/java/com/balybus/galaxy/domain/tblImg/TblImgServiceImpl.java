package com.balybus.galaxy.domain.tblImg;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.*;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.file.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TblImgServiceImpl {
    private final FileUploadUtils fileUploadUtils;
    private final AmazonS3Client amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /////////////////////////////////////

    /**
     * 로컬 이미지 등록
     * @param photoFiles MultipartFile[]
     * @return List<Long>
     */
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

    public Map<String, String> getPresignedUrl(String prefix, String fileName) {
        if (!prefix.isEmpty()) {
            fileName = createPath(prefix, fileName);
        }

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(bucket, fileName);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return Map.of("url", url.toString());
    }

    private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String bucket, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPresignedUrlExpiration());

        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString()
        );

        return generatePresignedUrlRequest;
    }

    private Date getPresignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);

        return expiration;
    }

    private String createFileId() {
        return UUID.randomUUID().toString();
    }

    private String createPath(String prefix, String fileName) {
        String fileId = createFileId();
        return String.format("%s/%s", prefix, fileId + "-" + fileName);
    }

    //////////

}
