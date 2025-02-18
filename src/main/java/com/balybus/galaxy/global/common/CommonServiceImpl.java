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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommonServiceImpl implements CommonService {

    private final TblImgServiceImpl imgService;

    private final HelperRepository helperRepository;
    private final TblCenterManagerRepository centerManagerRepository;

    private final FileUploadUtils fileUploadUtils;

    /**
     * 프로필 이미지 변경 (요양보호사, 관리자, 어르신)
     * 기존 등록된 이미지가 있는 경우, 삭제 / 새로운 이미지가 있는 경우, 등록
     * @param userDetails   UserDetails:토큰 추출 데이터
     * @param dto           ImgRequestDto.uploadUserImg
     * @return              ImgResponseDto.uploadUserImg
     */
    @Override
    @Transactional
    public ImgResponseDto.uploadUserImg uploadUserImg(UserDetails userDetails, ImgRequestDto.uploadUserImg dto) {
        //1. 이미지 개수 검사
        MultipartFile[] photoFiles = dto.getPhotoFiles();
        if(photoFiles != null && photoFiles.length > 1)
            throw new BadRequestException(ExceptionCode.TOO_MUCH_FILE);

        //2. 권한별 이미지 등록 가능여부 파악
        boolean authValidation = false;
        RoleType roleType = dto.getImgOwnerAuth();
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
        //      2-3. 그 외는 권한이 없다.
        if (!authValidation) throw new BadRequestException(ExceptionCode.UNAUTHORIZED);

        //3. 이미지 등록 주체에 따른 사용자 정보 조회
        Long seq = dto.getSeq();
        Long newImgSeq = null;
        if(RoleType.MEMBER == roleType) {
            newImgSeq = updateUserProfileImage(helperRepository, seq, photoFiles);
        } else if (RoleType.MANAGER == roleType) {
            newImgSeq = updateUserProfileImage(centerManagerRepository, seq, photoFiles);
        } else {
            log.info("어르신(TblPatient) entity 구현되면 추가 구현 예정");
//            newImgSeq = updateUserProfileImage(patientRepository, seq, photoFiles);
        }
        return ImgResponseDto.uploadUserImg.builder()
                .imgSeq(newImgSeq)
                .seq(dto.getSeq())
                .imgOwnerAuth(dto.getImgOwnerAuth())
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
    private <T extends ChangeProfileImg> Long updateUserProfileImage(
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
}
