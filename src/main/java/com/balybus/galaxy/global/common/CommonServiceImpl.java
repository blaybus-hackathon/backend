package com.balybus.galaxy.global.common;

import com.balybus.galaxy.address.domain.TblAddressFirst;
import com.balybus.galaxy.address.domain.TblAddressSecond;
import com.balybus.galaxy.address.domain.TblAddressThird;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.domain.tblImg.TblImg;
import com.balybus.galaxy.domain.tblImg.dto.ImgRequestDto;
import com.balybus.galaxy.domain.tblImg.dto.ImgResponseDto;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.file.ChangeProfileImg;
import com.balybus.galaxy.global.utils.file.service.FileService;
import com.balybus.galaxy.helper.repository.HelperRepository;
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

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

@Slf4j
@Service
@Transactional
@PropertySource("classpath:/application.yml")
public class CommonServiceImpl implements CommonService {
    @Value("${spring.profiles.active}")
    private String active;

    private final Map<RoleType, JpaRepository<? extends ChangeProfileImg, Long>> repositoryMap;
    private final FileService fileService;

    public CommonServiceImpl(HelperRepository helperRepository,
                             TblCenterManagerRepository centerManagerRepository,
                             TblPatientRepository patientRepository,
                             FileService fileService){
        // 필수 필드 초기화
        this.fileService = fileService;

        // repositoryMap 초기화
        this.repositoryMap = Map.of(
                RoleType.MEMBER, helperRepository,
                RoleType.MANAGER, centerManagerRepository,
                RoleType.PATIENT, patientRepository
        );

    }

    /**
     * 프로필 이미지 변경 및 업로드 API
     * @param userDetails UserDetails
     * @param roleType RoleType
     * @param dto ImgRequestDto.uploadUserImg
     * @return ImgResponseDto.UploadUserImg
     */
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

        //3. 이미지 업로드
        return uploadImage(repository, seq, dto.getPhotoFile());
    }

    // 권한별 이미지 등록 가능여부 파악
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

    /**
     * 서버에 이미지 파일 저장
     * @param repository JpaRepository
     * @param seq Long
     * @param photoFile MultipartFile
     * @return ImgResponseDto.UploadUserImg
     */
    public ImgResponseDto.UploadUserImg uploadImage(JpaRepository<? extends ChangeProfileImg, Long> repository,
                                                            Long seq, MultipartFile photoFile) {
        Long newImgSeq = updateUserProfileImage(repository, seq, photoFile);
        return ImgResponseDto.UploadUserImg.builder()
                .imgSeq(newImgSeq)
                .build();
    }

    // 저장된 파일 DB에 업데이트
    private <T extends ChangeProfileImg> Long updateUserProfileImage(
            JpaRepository<T, Long> repository, Long seq, MultipartFile photoFile) {
        //1. 엔티티 검색
        T entity = repository.findById(seq)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_TARGET));

        //2. 기존 이미지 삭제
        if(entity.getImg() != null)
            fileService.deleteFile(entity.getImg().getId());

        //3. 새로운 이미지 등록
        TblImg imgEntity = fileService.uploadOneImgFile(photoFile);
        entity.updateImg(imgEntity);

        //4. 새로운 이미지 구분자 값 반환
        return imgEntity == null ? null : imgEntity.getId();
    }

    /**
     * 나이 계산기(만 나이 반환)
     * @param birthDate LocalDate:생년월일
     * @return int:만 나이
     */
    @Override
    public int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        // 생일과 현재 날짜의 차이 계산
        Period period = Period.between(birthDate, currentDate);
        // 나이는 Period 객체의 연도를 반환
        return period.getYears();
    }

    /**
     * 주소 이름 정리
     * @param first TblAddressFirst:광역시.도
     * @param second TblAddressSecond:시.군.구
     * @param third TblAddressThird:읍.면.동
     * @return String
     */
    @Override
    public String fullAddressString(TblAddressFirst first, TblAddressSecond second, TblAddressThird third) {
        String address = first.getName();

        if(first.getId() * 1000 == second.getId()) address = second.getName();
        else if (second.getId() * 1000 == third.getId()) address += " " + third.getName();
        else address += " " + second.getName() + " " + third.getName();

        return address;
    }
}
