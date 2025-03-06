package com.balybus.galaxy.global.common;

import com.balybus.galaxy.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.domain.tblImg.TblImg;
import com.balybus.galaxy.domain.tblImg.TblImgServiceImpl;
import com.balybus.galaxy.domain.tblImg.dto.ImgRequestDto;
import com.balybus.galaxy.domain.tblImg.dto.ImgResponseDto;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.file.ChangeProfileImg;
import com.balybus.galaxy.global.utils.file.FileUploadUtils;
import com.balybus.galaxy.helper.repositoryImpl.HelperRepository;
import com.balybus.galaxy.login.domain.type.RoleType;
import com.balybus.galaxy.patient.domain.tblPatient.TblPatientRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@PropertySource("classpath:/application.yml")
public class CommonServiceImpl implements CommonService {
    @Value("${spring.profiles.active}")
    private String active;

    private final Map<RoleType, JpaRepository<? extends ChangeProfileImg, Long>> repositoryMap;
    private final TblImgServiceImpl imgService;
    private final FileUploadUtils fileUploadUtils;

    public CommonServiceImpl(HelperRepository helperRepository,
                             TblCenterManagerRepository centerManagerRepository,
                             TblPatientRepository patientRepository,
                             FileUploadUtils fileUploadUtils,
                             TblImgServiceImpl imgService){
        // 필수 필드 초기화
        this.fileUploadUtils = fileUploadUtils;
        this.imgService = imgService;

        // repositoryMap 초기화
        this.repositoryMap = Map.of(
                RoleType.MEMBER, helperRepository,
                RoleType.MANAGER, centerManagerRepository,
                RoleType.PATIENT, patientRepository
        );

    }

    @Override
    @Transactional
    public ImgResponseDto.UploadUserImg uploadUserImg(UserDetails userDetails, RoleType roleType, ImgRequestDto.uploadUserImg dto) {
        //1. 권한별 이미지 등록 가능여부 파악
        if (!imgAuthValidation(roleType, userDetails))
            throw new BadRequestException(ExceptionCode.UNAUTHORIZED);

        //2. 권한별 데이처 추출
        JpaRepository<? extends ChangeProfileImg, Long> repository = repositoryMap.getOrDefault(roleType, null);
        Long seq = switch (roleType) {
            case MEMBER -> dto.getHelperSeq();
            case MANAGER -> dto.getManagerSeq();
            case PATIENT -> dto.getPatientSeq();
            default -> throw new BadRequestException(ExceptionCode.UNAUTHORIZED);
        };

        //3. 활성화 상태에 따른 저장 방식 분기
        if ("dev".equals(active)) {
            return uploadImageToServer(repository, seq, dto.getFileName());
        } else {
            return uploadImageToLocal(repository, seq, dto.getPhotoFiles());
        }
    }

    private ImgResponseDto.UploadUserImg uploadImageToServer(JpaRepository<? extends ChangeProfileImg, Long> repository,
                                                             Long seq, String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new BadRequestException(ExceptionCode.NEED_FILE_NAME);
        }

        Map<String, Object> updateImg = updateDevUserProfileImage(repository, seq, fileName);

        return ImgResponseDto.UploadUserImg.builder()
                .imgSeq((Long) updateImg.get("imgSeq"))
                .preSignedUrl(updateImg.get("preSignedUrl").toString())
                .build();
    }

    private ImgResponseDto.UploadUserImg uploadImageToLocal(JpaRepository<? extends ChangeProfileImg, Long> repository,
                                                            Long seq, MultipartFile[] photoFiles) {
        if (photoFiles != null && photoFiles.length > 1) {
            throw new BadRequestException(ExceptionCode.TOO_MUCH_FILE);
        }

        Long newImgSeq = updateLocalUserProfileImage(repository, seq, photoFiles);

        return ImgResponseDto.UploadUserImg.builder()
                .imgSeq(newImgSeq)
                .build();
    }

    /**
     * 프로필 이미지 변경(기존 등록된 이미지가 있는 경우, 삭제 / 새로운 이미지가 있는 경우, 등록)
     * @param repository    엔티티를 조회할 JpaRepository
     * @param seq           업데이트할 사용자의 ID
     * @param photoFiles    새로운 프로필 이미지 파일 목록
     * @param <T>           프로필 이미지를 가진 엔티티 타입
     * @return              새로 등록한 이미지 파일 ID
     */
    private <T extends ChangeProfileImg> Long updateLocalUserProfileImage(
            JpaRepository<T, Long> repository, Long seq, MultipartFile[] photoFiles) {
        //1. 엔티티 검색
        T entity = repository.findById(seq)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_TARGET));

        //2. 기존 이미지 삭제
        if(entity.getImg() != null)
            fileUploadUtils.deleteFile(entity.getImg().getId());

        //3. 새로운 이미지 등록
        List<Long> newImgSeqList = imgService.uploadImg(photoFiles);
        entity.updateImg((newImgSeqList.size() == 1)
                ? TblImg.builder()
                    .id(newImgSeqList.get(0))
                    .build() : null);

        //4. 새로운 이미지 구분자 값 반환
        return entity.getImg().getId();
    }

    /**
     * 프로필 이미지 변경(기존 등록된 이미지가 있는 경우, 삭제 / 새로운 이미지가 있는 경우, 등록)
     * @param repository    엔티티를 조회할 JpaRepository
     * @param seq           업데이트할 사용자의 ID
     * @param fileName      새로운 프로필 이미지 파일명
     * @param <T>           프로필 이미지를 가진 엔티티 타입
     * @return              새로 등록한 이미지 파일 ID
     */
    private <T extends ChangeProfileImg> Map<String, Object> updateDevUserProfileImage(
            JpaRepository<T, Long> repository, Long seq, String fileName) {
        //1. 엔티티 검색
        T entity = repository.findById(seq)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_TARGET));

        //2. 기존 이미지 삭제
        if(entity.getImg() != null)
            fileUploadUtils.deleteDevFile(entity.getImg());

        //3. 새로운 이미지 등록을 위한 PreSigned Url 생성
        Map<String, Object> preSignedUrl = fileUploadUtils.getPresignedUrl("images", fileName);
        entity.updateImg( preSignedUrl == null ? null : (TblImg) preSignedUrl.get("imgEntity"));

        //4. 새로운 이미지 구분자 값 반환
        return Map.of(
                "imgSeq", entity.getImg().getId(),
                "preSignedUrl", preSignedUrl == null ? null : preSignedUrl.get("preSignedUrl")
        );
    }



    /**
     * 권한별 이미지 등록 가능여부 파악
     * @param roleType RoleType
     * @param userDetails UserDetails
     * @return boolean
     */
    private boolean imgAuthValidation(RoleType roleType, UserDetails userDetails) {
        boolean authValidation = false;

        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if(RoleType.MEMBER.getCode().equals(authority.getAuthority())){
                //2-1. 요양보호사:MEMBER 로그인의 경우, 요양보호사 이미지만 등록 가능
                authValidation = (RoleType.MEMBER == roleType);
                break;
            } else if (RoleType.MANAGER.getCode().equals(authority.getAuthority())){
                //2-2. 관리자:MANAGER 로그인의 경우, 관리자와 어르신 이미지만 등록 가능
                authValidation = (RoleType.MANAGER == roleType || RoleType.PATIENT == roleType);
                break;
            }
        }

        return authValidation;
    }
}
