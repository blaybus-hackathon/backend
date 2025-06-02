package com.balybus.galaxy.global.utils.file.service.serviceImpl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.balybus.galaxy.global.domain.tblImg.TblImg;
import com.balybus.galaxy.global.domain.tblImg.TblImgRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.file.service.AbstractFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

@Slf4j
@Service
@Profile("dev")
public class DevFileServerImpl extends AbstractFileService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3;

    public DevFileServerImpl(TblImgRepository photoFileRepository,
                             AmazonS3Client amazonS3) {
        super(photoFileRepository);
        this.amazonS3 = amazonS3;
    }

    @Override
    public TblImg uploadOneImgFileInternal(MultipartFile file) {
        //1. DB에 저장이 필요한 파일명 처리
        String origFilename = file.getOriginalFilename();               // 원본 파일명
        String extension = file.getContentType().split("/")[1];   // 파일 확장자
        String fileUuid = createPath("image", origFilename);      // 서버에 저장할 파일명 - 파일명이 동일하면 덮어쓰기가 이뤄지는 위험 방지를 위함.

        //2. 파일 메타데이터 설정 (파일 크기)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        //3. S3에 파일 업로드
        try {
            amazonS3.putObject(new PutObjectRequest(bucket, fileUuid, file.getInputStream(), metadata));
        } catch (IllegalStateException | IOException e) {
            log.info(">>>>>>> fileUpload 오류 :: ", e);
            throw new BadRequestException(ExceptionCode.UPLOAD_FAILED);
        }

        //4. entity 저장 및 반환
        return photoFileRepository.save(TblImg.builder()
                .imgOriginName(origFilename)
                .imgUuid(fileUuid)
                .build());
    }

    private String createPath(String prefix, String fileName) {
        String fileId = getUuid();
        return String.format("%s/%s", prefix, fileId + "-" + fileName);
    }

    /**
     * S3 파일 삭제
     * @param fileEntity TblImg
     */
    @Override
    public void deleteFileInternal(TblImg fileEntity) {
        if(fileEntity != null && fileEntity.getId() != null) {
            amazonS3.deleteObject(bucket, fileEntity.getImgUuid());
            photoFileRepository.delete(fileEntity);
        }
    }

    @Override
    public String getOneImgUrlInternal(String fileUuid) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileUuid)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 24시간 유효

        URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return presignedUrl.toString();
    }
}
