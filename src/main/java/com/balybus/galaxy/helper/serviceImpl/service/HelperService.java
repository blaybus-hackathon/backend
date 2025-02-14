package com.balybus.galaxy.helper.serviceImpl.service;

import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.domain.TblHelperExperience;
import com.balybus.galaxy.helper.domain.TblHelperWorkLocation;
import com.balybus.galaxy.helper.domain.TblHelperWorkTime;
import com.balybus.galaxy.helper.dto.request.HelperExperienceDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkLocationDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkTimeRequestDTO;
import com.balybus.galaxy.helper.dto.response.HelperExperienceResponse;
import com.balybus.galaxy.helper.dto.response.HelperWorkLocationReponse;
import com.balybus.galaxy.helper.dto.response.HelperWorkTimeResponse;
import com.balybus.galaxy.helper.repository.HelperExperienceRepository;
import com.balybus.galaxy.helper.repository.HelperRepository;
import com.balybus.galaxy.helper.repository.HelperWorkLocationRepository;
import com.balybus.galaxy.helper.repository.HelperWorkTimeRepository;
import com.balybus.galaxy.helper.serviceImpl.HelperServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import static com.balybus.galaxy.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class HelperService implements HelperServiceImpl {

    private final HelperRepository helperRepository;
    private final HelperWorkLocationRepository helperWorkLocationRepository;
    private final HelperWorkTimeRepository helperWorkTimeRepository;
    private final HelperExperienceRepository helperExperienceRepository;

    @Override
    public HelperWorkLocationReponse workLocationSignUp(HelperWorkLocationDTO helperWorkLocationDTO) {
        // 1. Helper 테이블 찾기, 없으면 예외 발생
        TblHelper tblHelper = helperRepository.findById(helperWorkLocationDTO.getHelperId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        TblHelperWorkLocation tblHelperWorkLocation = TblHelperWorkLocation.builder()
                .helper(tblHelper)
                .tblAddressFirst(helperWorkLocationDTO.getTblAddressFirst())
                .tblAddressSecond(helperWorkLocationDTO.getTblAddressSecond())
                .tblAddressThird(helperWorkLocationDTO.getTblAddressThird())
                .build();

        TblHelperWorkLocation tblHelperWorkLocationSaved = helperWorkLocationRepository.save(tblHelperWorkLocation);

        return HelperWorkLocationReponse.builder()
                .helperName(tblHelperWorkLocationSaved.getHelper().getName())
                .build();
    }

    @Override
    public HelperWorkTimeResponse workTimeSignUp(HelperWorkTimeRequestDTO helperWorkTimeRequestDTO) {
        // 1. Helper 테이블 찾기, 없으면 예외 발생
        TblHelper tblHelper = helperRepository.findById(helperWorkTimeRequestDTO.getHelperId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        // 1-2 Helper테이블이 1개 이상의 WorkTime 테이블을 가지고 있으면 중복 처리로 에러 발생
        int countHelperWorTime = helperWorkTimeRepository.countByHelperAndDateAndStartTimeAndEndTime(
                tblHelper,
                helperWorkTimeRequestDTO.getDate(),
                helperWorkTimeRequestDTO.getStartTime(),
                helperWorkTimeRequestDTO.getEndTime()
        );

        if(countHelperWorTime > 0) {
            throw new BadRequestException(HELPER_ALREADY_HAS_WORK_TIME);
        }

        // 2. 엔티티 만들어서 회원 가입 진행, 가입 전 null값 체크
        if(HelperWorkTimeRequestDTO.hasNullHelperWorkTimeRequestDTO(helperWorkTimeRequestDTO)) {
            throw new BadRequestException(SIGNUP_HELPER_WORK_TIME_INFO_NULL);
        }

        TblHelperWorkTime tblHelperWorkTime = TblHelperWorkTime.builder()
                .helper(tblHelper)
                .date(helperWorkTimeRequestDTO.getDate())
                .startTime(helperWorkTimeRequestDTO.getStartTime())
                .endTime(helperWorkTimeRequestDTO.getEndTime())
                .build();

        try {
            TblHelperWorkTime tblHelperWorkTimeSaved = helperWorkTimeRepository.save(tblHelperWorkTime);

            return HelperWorkTimeResponse.builder()
                    .helperName(tblHelperWorkTimeSaved.getHelper().getName())
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(DATA_VALIDATION_ERROR);

        } catch (TransactionSystemException e) {
            throw new BadRequestException(TRANSACTIONAL_ERROR);

        } catch (Exception e) {
            throw new BadRequestException(INTERNAL_SEVER_ERROR);
        }
    }

    @Override
    public HelperExperienceResponse experienceSignUp(HelperExperienceDTO helperExperienceDTO) {
        // 1. Helper 테이블 찾기, 없으면 예외 발생
        TblHelper tblHelper = helperRepository.findById(helperExperienceDTO.getHelperId())
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
