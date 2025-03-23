package com.balybus.galaxy.global.utils.file.service;

import com.balybus.galaxy.domain.tblImg.TblImg;
import com.balybus.galaxy.domain.tblImg.TblImgRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
public abstract class AbstractFileService implements FileService{
    protected final TblImgRepository photoFileRepository;
    public AbstractFileService(TblImgRepository photoFileRepository) {
        this.photoFileRepository = photoFileRepository;
    }

    @Override
    public TblImg uploadOneImgFile(MultipartFile file) {
        TblImg resultDto = null;
        //1. 파일이 없을 경우, 실행 안함.
        if (file.getSize() >= 1) {
            String[] contentType = file.getContentType().split("/");
            //2. 이미지 파일이 아닌 경우, 등록 안함.
            if ("image".equals(contentType[0])) {
                resultDto = uploadOneImgFileInternal(file);
            }
        }
        return resultDto;
    }

    public abstract TblImg uploadOneImgFileInternal(MultipartFile fileEntity);


    /**
     * 파일 삭제 공통 구현 코드
     * @param fileSeq Long
     */
    @Override
    public void deleteFile(Long fileSeq) {
        // 1. 삭제 요청한 데이터가 있는지 확인
        TblImg fileEntity = photoFileRepository.findById(fileSeq)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.FILE_NOT_FOUND));

        // 2. 데이터 삭제
        log.info("파일 삭제 요청: {}", fileSeq);
        deleteFileInternal(fileEntity);
    }

    // 각 환경별 삭제 로직은 하위 클래스에서 구현
    public abstract void deleteFileInternal(TblImg fileEntity);

    /**
     * 공통적으로 사용될 파일 이름 생성 로직
     * @return String
     */
    protected  String getUuid() {
        return (LocalDate.now()+ UUID.randomUUID().toString()).replaceAll("-", "");
    }
}
