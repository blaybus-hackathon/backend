package com.balybus.galaxy.global.utils.file;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.balybus.galaxy.domain.tblImg.TblImg;
import com.balybus.galaxy.domain.tblImg.TblImgRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@PropertySource("classpath:/application.yml")
public class FileUploadUtils {
    @Value("${spring.locations.file-archive}")
    private String archive;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final TblImgRepository photoFileRepository;
    private final AmazonS3Client amazonS3;

    private String getUuid() {
        return (LocalDate.now()+ UUID.randomUUID().toString()).replaceAll("-", "");
    }

    /* ==========================================================================================
     * LOCAL
     * ========================================================================================== */

    /**
     * 이미지 업로드 (저장)
     * @param files - MultipartFile[] : 파일 배열
     */
    public List<Long> uploadFiles(MultipartFile[] files) {

        // 1. uploadPath(archive)에 해당하는 디렉터리가 존재하지 않는 경우, 부모 디렉터리를 포함한 모든 디렉터리를 생성
        File dir = new File(archive);
        if (!dir.exists()) {
            boolean folderExist = dir.mkdirs();
            log.info("folderExist :: {}", folderExist);
        }

        // 2. Save할 Entity를 담을 리스트
        List<TblImg> fileList = new ArrayList<>();

        // 3. 파일 개수만큼 forEach
        for (MultipartFile file : files) {
            // 3-1. 이미지 파일이 아닌 경우, 등록 안함.
            if (file.getSize() < 1 || file.getContentType() == null) continue;
            String[] contentType = file.getContentType().split("/");
            if (!"image".equals(contentType[0])) continue;

            // 3-2. DB에 저장이 필요한 파일명 처리
            String origFilename = file.getOriginalFilename();   // 원본 파일명
            String extension = contentType[1];                  // 파일 확장자
            String fileUuid = getUuid() + "." + extension;      // 서버에 저장할 파일명 (날짜 + 랜덤 문자열 + 확장자) - 파일명이 동일하면 덮어쓰기가 이뤄지는 위험 방지를 위함.

            try {
                // 3-3. 업로드 경로에 fileUuid와 동일한 이름으로 파일 생성
                file.transferTo(new File(archive, fileUuid));

                // 3-4. Save 처리할 Entity 정보 저장
                fileList.add(
                        TblImg.builder()
                                .imgOriginName(origFilename)
                                .imgUuid(fileUuid)
                                .build());
            } catch (IllegalStateException | IOException e) {
                log.info(">>>>>>> fileUpload 오류 :: ", e);
                throw new BadRequestException(ExceptionCode.UPLOAD_FAILED);
            }
        }

        List<TblImg> imgEntityList = photoFileRepository.saveAll(fileList);
        return imgEntityList.stream()
                .map(TblImg::getId)
                .collect(Collectors.toList());
    }

    /**
     * 이미지 조회
     * @param entity - PhotoFile : 바이트로 변환하려는 포토 entity
     */
    public byte[] viewImage(TblImg entity) {
        try {
            String uploadedFilePath = Paths.get(archive, entity.getImgUuid()).toString();
            return Files.readAllBytes(new File(uploadedFilePath).toPath());
        } catch (IOException e) {
            throw new BadRequestException(ExceptionCode.FILE_NOT_FOUND);
        }
    }

    /**
     * 파일 삭제
     * @param fileSeq - Long : 삭제하려는 이미지 시퀀스 넘버
     */
    public void deleteFile(Long fileSeq) {
        // 1. 삭제 요청한 데이터가 있는지 확인
        TblImg fileEntity = photoFileRepository.findById(fileSeq)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.FILE_NOT_FOUND));

        // 2. 데이터 삭제
        deleteFile(fileEntity);
    }

    /**
     * 파일 삭제
     * @param fileEntity - PhotoFile : 삭제하려는 이미지 entity
     */
    public void deleteFile(TblImg fileEntity) {
        // 1. 업로드된 파일 경로 획득
        String uploadedFilePath = Paths.get(archive, fileEntity.getImgUuid()).toString();

        // 2. 파일 찾기
        File file = new File(uploadedFilePath);

        // 3. 파일 및 DB 정보 삭제
        if (file.exists()) {
            boolean deleteRe = file.delete();
            // 파일이 삭제되면, DB 에서 정보 삭제
            if (deleteRe) photoFileRepository.delete(fileEntity);
        }else {
            log.debug("FILE NOT FOUND");
            log.debug("savedFilename : {}", fileEntity.getImgUuid());
            photoFileRepository.delete(fileEntity);
        }
    }




    /* ==========================================================================================
     * DEV
     * ========================================================================================== */

    /**
     * S3 PreSigned Url 생성
     * @param prefix String
     * @param fileName String
     * @return String
     */
    public Map<String, Object> getPresignedUrl(String prefix, String fileName) {
        if(fileName == null || fileName.isEmpty()) return null;

        String imgUuid = createPath(prefix==null ? "" : prefix, fileName);

        TblImg saveImg = TblImg.builder()
                .imgOriginName(fileName)
                .imgUuid(imgUuid)
                .build();
        photoFileRepository.save(saveImg);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(bucket, imgUuid);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return Map.of(
                "preSignedUrl", url.toString(),
                "imgEntity", saveImg
        );
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

    private String createPath(String prefix, String fileName) {
        String fileId = getUuid();
        return String.format("%s/%s", prefix, fileId + "-" + fileName);
    }

    /**
     * S3 파일 삭제
     * @param fileEntity TblImg
     */
    public void deleteDevFile(TblImg fileEntity) {
        amazonS3.deleteObject(bucket, fileEntity.getImgUuid());
    }
}
