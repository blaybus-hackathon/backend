package com.balybus.galaxy.global.utils;

import com.balybus.galaxy.domain.tblImg.TblImg;
import com.balybus.galaxy.domain.tblImg.TblImgRepository;
import com.balybus.galaxy.global.exception.CustomException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@PropertySource("classpath:/application.yml")
public class FileUploadUtils {
    @Value("${spring.locations.file-archive}")
    private String archive;

    @Autowired
    private TblImgRepository photoFileRepository;

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
                throw new CustomException(ExceptionCode.UPLOAD_FAILED);
            }
        }

        List<TblImg> imgEntityList = photoFileRepository.saveAll(fileList);
        return imgEntityList.stream()
                .map(TblImg::getId)
                .collect(Collectors.toList());
    }

    private String getUuid() {
        return (LocalDate.now()+ UUID.randomUUID().toString()).replaceAll("-", "");
    }
}
