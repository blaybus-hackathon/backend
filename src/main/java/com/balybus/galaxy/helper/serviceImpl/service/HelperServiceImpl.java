package com.balybus.galaxy.helper.serviceImpl.service;

import com.balybus.galaxy.address.domain.TblAddressSecond;
import com.balybus.galaxy.address.domain.TblAddressThird;
import com.balybus.galaxy.address.repository.TblAddressFirstRepository;
import com.balybus.galaxy.address.repository.TblAddressSecondRepository;
import com.balybus.galaxy.address.repository.TblAddressThirdRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.domain.TblHelperExperience;
import com.balybus.galaxy.helper.domain.TblHelperWorkLocation;
import com.balybus.galaxy.helper.domain.TblHelperWorkTime;
import com.balybus.galaxy.helper.dto.request.*;
import com.balybus.galaxy.helper.dto.response.*;
import com.balybus.galaxy.helper.repositoryImpl.HelperExperienceRepository;
import com.balybus.galaxy.helper.repositoryImpl.HelperRepository;
import com.balybus.galaxy.helper.repositoryImpl.HelperWorkLocationRepository;
import com.balybus.galaxy.helper.repositoryImpl.HelperWorkTimeRepository;
import com.balybus.galaxy.helper.serviceImpl.HelperService;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.*;
import java.util.stream.Collectors;

import static com.balybus.galaxy.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class HelperServiceImpl implements HelperService {

    private final HelperRepository helperRepository;
    private final MemberRepository memberRepository;

    private final HelperWorkLocationRepository helperWorkLocationRepository;
    private final HelperWorkTimeRepository helperWorkTimeRepository;
    private final HelperExperienceRepository helperExperienceRepository;

    private final TblAddressFirstRepository tblAddressFirstRepository;
    private final TblAddressSecondRepository tblAddressSecondRepository;
    private final TblAddressThirdRepository tblAddressThirdRepository;

    @Override
    public HelperResponse getAllHelperInfo(UserDetails userDetails) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        return HelperResponse.builder()
                .id(tblHelper.getId())
                .userEmail(tblUser.getEmail())
                .name(tblHelper.getName())
                .phone(tblHelper.getPhone())
                .addressDetail(tblHelper.getAddressDetail())
                .essentialCertNo(tblHelper.getEssentialCertNo())
                .careCertNo(tblHelper.getCareCertNo())
                .nurseCertNo(tblHelper.getNurseCertNo())
                .postPartumCertNo(tblHelper.getPostPartumCertNo())
                .helperOtherCerts(tblHelper.getHelperOtherCerts())
                .carOwnYn(tblHelper.isCarOwnYn())
                .eduYn(tblHelper.isEduYn())
                .wage(tblHelper.getWage())
                .build();
    }

    @Override
    public void updateProfile(UserDetails userDetails, HelperProfileDTO helperProfileDTO) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        tblHelper.setIntroduce(helperProfileDTO.getIntroduce());
        tblHelper.setEssentialCertNo(helperProfileDTO.getEssentialCertNo());
        tblHelper.setStrengths(helperProfileDTO.getStrengths());
        tblHelper.setCareCertNo(helperProfileDTO.getCareCertNo());
        tblHelper.setNurseCertNo(helperProfileDTO.getNurseCertNo());
        tblHelper.setPostPartumCertNo(helperProfileDTO.getPostPartumCertNo());
        tblHelper.setHelperOtherCerts(helperProfileDTO.getHelperOtherCerts());

        helperRepository.save(tblHelper);
    }

    @Override
    public WageUpdateResponse updateWage(UserDetails userDetails, WageUpdateDTO wageUpdateDTO) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        Map<Integer, String> helperWage = new HashMap<>();
        helperWage.put(wageUpdateDTO.getUnit(), wageUpdateDTO.getWage());

        tblHelper.setWage(helperWage);
        tblHelper.setWageNegotiation(wageUpdateDTO.getWageNegotiation());

        helperRepository.save(tblHelper);

        return WageUpdateResponse.builder()
                .email(tblUser.getEmail())
                .wage(wageUpdateDTO.getWage())
                .build();
    }

    @Override
    public HelperWorkLocationReponse workLocationSignUp(HelperWorkLocationDTO helperWorkLocationDTO, UserDetails userDetails) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        List<com.balybus.galaxy.address.domain.TblAddressFirst> addressFirstList = tblAddressFirstRepository.findAllById(helperWorkLocationDTO.getAddressFirstIds());
        List<TblAddressSecond> addressSecondList = tblAddressSecondRepository.findAllById(helperWorkLocationDTO.getAddressSecondIds());
        List<TblAddressThird> addressThirds = tblAddressThirdRepository.findAllById(helperWorkLocationDTO.getAddressThirdIds());

        if(addressFirstList.isEmpty() || addressSecondList.isEmpty() || addressThirds.isEmpty()) {
            throw new BadRequestException(INVALID_ADDR_INFO);
        }

        List<TblHelperWorkLocation> workLocations = addressFirstList.stream()
                .flatMap(first -> addressSecondList.stream()
                        .flatMap(second -> addressThirds.stream()
                                .map(third -> TblHelperWorkLocation.builder()
                                        .helper(tblHelper)
                                        .tblAddressFirst(first)
                                        .tblAddressSecond(second)
                                        .tblAddressThird(third)
                                        .build()))).toList();

        if(workLocations.isEmpty()) {
            throw new BadRequestException(NO_ADDRESS_INFO);
        }

        helperWorkLocationRepository.saveAll(workLocations);

        return HelperWorkLocationReponse.builder()
                .helperName(tblHelper.getName())
                .build();
    }

    @Override
    public List<TblAddressFirstResponse> getFirstAddress() {
        return tblAddressFirstRepository.findAll().stream()
                .map(TblAddressFirstResponse::new)  // DTO로 변환
                .collect(Collectors.toList());
    }

    @Override
    public List<TblAddressThirdResponse> getThirdAddressBySecondId(Long asSeq) {
        List<TblAddressThird> entities = tblAddressThirdRepository.findByAddressSecond_Id(asSeq);

        return entities.stream()
                .map(TblAddressThirdResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TblAddressSecondResponse> getAddressSecondByFirstId(Long afSeq) {
        List<TblAddressSecond> entities = tblAddressSecondRepository.findByAddressFirst_Id(afSeq);

        return entities.stream()
                .map(TblAddressSecondResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public HelperWorkTimeResponse workTimeSignUp(HelperWorkTimeRequestDTO helperWorkTimeRequestDTO, UserDetails userDetails) {
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        // 1. 중복되지 않는 새로운 데이터만 필터링
        List<TblHelperWorkTime> newWorkTimes = helperWorkTimeRequestDTO.getWorkTimes().stream()
                .filter(dto -> !helperWorkTimeRepository.existsByHelperAndDateAndStartTimeAndEndTime(
                        tblHelper, dto.getDay(), dto.getStartTime(), dto.getEndTime()))
                .map(dto -> TblHelperWorkTime.builder()
                        .helper(tblHelper)
                        .date(dto.getDay())
                        .startTime(dto.getStartTime())
                        .endTime(dto.getEndTime())
                        .negotiation(helperWorkTimeRequestDTO.getNegotiation())
                        .workTerm(helperWorkTimeRequestDTO.getWorkTerm())
                        .build())
                .collect(Collectors.toList());

        // 2. 저장할 데이터가 존재할 때만 저장
        if (!newWorkTimes.isEmpty()) {
            helperWorkTimeRepository.saveAll(newWorkTimes);
            return HelperWorkTimeResponse.builder()
                    .helperName(tblHelper.getName())
                    .build();
        }
        throw new BadRequestException(DUPLICATE_WORK_TIME);
    }



    @Override
    public HelperExperienceResponse experienceSignUp(HelperExperienceDTO helperExperienceDTO, UserDetails userDetails) {
        // 1. Helper 테이블 찾기, 없으면 예외 발생
        String username = userDetails.getUsername();

        TblUser tblUser = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));

        TblHelper tblHelper = helperRepository.findByUserId(tblUser.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        // 1-2 Helper테이블이 1개 이상의 Experience 테이블을 가지고 있으면 중복 처리로 에러 발생
        int countExperience = helperExperienceRepository.countByHelperAndFieldAndHeStartDateAndHeEndDate(
                tblHelper,
                helperExperienceDTO.getField(),
                helperExperienceDTO.getHeStartDate(),
                helperExperienceDTO.getHeEndDate()
                );
        if(countExperience > 0) {
            throw new BadRequestException(HELPER_ALREADY_HAS_EXPERIENCE);
        }

        // 2. 엔티티 만들어서 회원 가입 진행, 가입 전 null값 체크
        if(HelperExperienceDTO.hasNullHelperExperienceRequestDTO(helperExperienceDTO)) {
            throw new BadRequestException(SIGNUP_HELPER_EXPERIENCE_INFO_NULL);
        }

        TblHelperExperience tblHelperExperience = TblHelperExperience.builder()
                .helper(tblHelper)
                .field(helperExperienceDTO.getField())
                .heStartDate(helperExperienceDTO.getHeStartDate())
                .heEndDate(helperExperienceDTO.getHeEndDate())
                .build();

        try {
            TblHelperExperience tblHelperExperienceSaved = helperExperienceRepository.save(tblHelperExperience);

            return HelperExperienceResponse.builder()
                    .helperName(tblHelperExperienceSaved.getHelper().getName())
                    .filed(tblHelperExperienceSaved.getField())
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(DATA_VALIDATION_ERROR);

        } catch (TransactionSystemException e) {
            throw new BadRequestException(TRANSACTIONAL_ERROR);

        } catch (Exception e) {
            throw new BadRequestException(INTERNAL_SEVER_ERROR);
        }
    }
}
