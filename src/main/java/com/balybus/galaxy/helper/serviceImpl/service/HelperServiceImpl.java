package com.balybus.galaxy.helper.serviceImpl.service;

import com.balybus.galaxy.address.domain.TblAddressFirst;
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
import com.balybus.galaxy.helper.dto.request.HelperExperienceDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkLocationDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkTimeDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkTimeRequestDTO;
import com.balybus.galaxy.helper.dto.response.AddressResponseDTO;
import com.balybus.galaxy.helper.dto.response.HelperExperienceResponse;
import com.balybus.galaxy.helper.dto.response.HelperWorkLocationReponse;
import com.balybus.galaxy.helper.dto.response.HelperWorkTimeResponse;
import com.balybus.galaxy.helper.repository.HelperExperienceRepository;
import com.balybus.galaxy.helper.repository.HelperRepository;
import com.balybus.galaxy.helper.repository.HelperWorkLocationRepository;
import com.balybus.galaxy.helper.repository.HelperWorkTimeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;
import java.util.stream.Collectors;

import static com.balybus.galaxy.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class HelperServiceImpl implements com.balybus.galaxy.helper.serviceImpl.HelperService {

    private final HelperRepository helperRepository;

    private final HelperWorkLocationRepository helperWorkLocationRepository;
    private final HelperWorkTimeRepository helperWorkTimeRepository;
    private final HelperExperienceRepository helperExperienceRepository;

    private final TblAddressFirstRepository tblAddressFirstRepository;
    private final TblAddressSecondRepository tblAddressSecondRepository;
    private final TblAddressThirdRepository tblAddressThirdRepository;

    /**
     * 사용자가 선택한 주소를 반환
     * @param helperWorkLocationDTO
     * @return
     */
    @Override
    public HelperWorkLocationReponse workLocationSignUp(HelperWorkLocationDTO helperWorkLocationDTO) {
        // 1. Helper 테이블 찾기, 없으면 예외 발생
        TblHelper tblHelper = helperRepository.findById(helperWorkLocationDTO.getHelperId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        // 2. dto 리스트 값과 대응하는 테이블 데이터 찾기
        List<TblAddressFirst> addressFirstList = tblAddressFirstRepository.findAllById(helperWorkLocationDTO.getAddressFirstIds());
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
    public AddressResponseDTO getAllAddress() {
        List<TblAddressFirst> tblAddressFirstList = tblAddressFirstRepository.findAll();
        List<TblAddressSecond> tblAddressSecondList = tblAddressSecondRepository.findAll();
        List<TblAddressThird> tblAddressThirdList = tblAddressThirdRepository.findAll();

        return AddressResponseDTO.builder()
                .addressFirstList(tblAddressFirstList)
                .addressSecondList(tblAddressSecondList)
                .addressThirdList(tblAddressThirdList)
                .build();
    }


    @Override
    public HelperWorkTimeResponse workTimeSignUp(HelperWorkTimeRequestDTO helperWorkTimeRequestDTO) {
        // 1. Helper 테이블 찾기, 없으면 예외 발생
        TblHelper tblHelper = helperRepository.findById(helperWorkTimeRequestDTO.getHelperId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        // 2. 기존에 등록된 WorkTime 조회
        List<TblHelperWorkTime> existingWorkTimes = helperWorkTimeRepository.findByHelper(tblHelper);

        // 3. 새로운 데이터 중 기존 데이터와 중복되지 않는 것만 필터링
        List<TblHelperWorkTime> newWorkTimes = helperWorkTimeRequestDTO.getWorkTimes().stream()
                .filter(workTimeDTO -> existingWorkTimes.stream()
                        .noneMatch(existing -> existing.getDate().equals(workTimeDTO.getDay()) &&
                                existing.getStartTime().equals(workTimeDTO.getStartTime()) &&
                                existing.getEndTime().equals(workTimeDTO.getEndTime())))
                .map(workTimeDTO -> TblHelperWorkTime.builder()
                        .helper(tblHelper)
                        .date(workTimeDTO.getDay())
                        .startTime(workTimeDTO.getStartTime())
                        .endTime(workTimeDTO.getEndTime())
                        .build())
                .collect(Collectors.toList());

        // 4. 새로운 데이터만 저장
        if (!newWorkTimes.isEmpty()) {
            helperWorkTimeRepository.saveAll(newWorkTimes);
        }

        return HelperWorkTimeResponse.builder()
                .helperName(tblHelper.getName())
                .build();
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
