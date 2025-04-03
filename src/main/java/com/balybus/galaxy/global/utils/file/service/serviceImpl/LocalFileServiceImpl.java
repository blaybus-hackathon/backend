package com.balybus.galaxy.global.utils.file.service.serviceImpl;

import com.balybus.galaxy.domain.tblImg.TblImg;
import com.balybus.galaxy.domain.tblImg.TblImgRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.file.service.AbstractFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
@Profile("local")
@PropertySource("classpath:/application.yml")
public class LocalFileServiceImpl extends AbstractFileService {
    @Value("${spring.locations.file-archive}")
    private String archive;

    public LocalFileServiceImpl(TblImgRepository photoFileRepository) {
        super(photoFileRepository);
    }

    /**
     * 이미지 업로드 (저장)
     * @param file - MultipartFile : 파일
     */
    @Override
    public TblImg uploadOneImgFileInternal(MultipartFile file) {
        //1. uploadPath(archive)에 해당하는 디렉터리가 존재하지 않는 경우, 부모 디렉터리를 포함한 모든 디렉터리를 생성
        File dir = new File(archive);
        if (!dir.exists()) {
            boolean folderExist = dir.mkdirs();
            log.info("folderExist :: {}", folderExist);
        }

        //2. DB에 저장이 필요한 파일명 처리
        String origFilename = file.getOriginalFilename();               // 원본 파일명
        String extension = file.getContentType().split("/")[1];   // 파일 확장자
        String fileUuid = getUuid() + "." + extension;                  // 서버에 저장할 파일명 (날짜 + 랜덤 문자열 + 확장자) - 파일명이 동일하면 덮어쓰기가 이뤄지는 위험 방지를 위함.

        //3. 업로드 경로에 fileUuid와 동일한 이름으로 파일 생성
        try {
            file.transferTo(new File(archive, fileUuid));
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
     * @param fileEntity - PhotoFile : 삭제하려는 이미지 entity
     */
    @Override
    public void deleteFileInternal(TblImg fileEntity) {
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

    @Override
    public String getOneImgUrlInternal(String fileUuid) {
        return Paths.get(archive, fileUuid).toString();
    }
}
