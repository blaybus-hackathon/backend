package com.balybus.galaxy.global.domain.tblCare.service;

import com.balybus.galaxy.global.domain.tblCare.TblCareRepository;
import com.balybus.galaxy.global.domain.tblCare.TblCareTopEnum;
import com.balybus.galaxy.global.domain.tblCare.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TblCareServiceImpl implements TblCareService{
    private final TblCareRepository careRepository;

    /**
     * 선택한 돌봄 항목 항목별 pk 값 리스트 반환
     * @param careDto CareBaseDto
     * @param needWelfare boolean:복리후생 데이터 반환 여부
     * @return CareChoiceListDto
     */
    @Override
    public CareChoiceListDto getCareChoiceList(CareBaseDto careDto, boolean needWelfare){
        CareChoiceListDto resultDto = new CareChoiceListDto();
        for(TblCareTopEnum careTopEnum : TblCareTopEnum.values()) {
            switch (careTopEnum) {
                case WORK_TYPE:
                    resultDto.setWorkTypeList(careRepository.findByCareIdList(careTopEnum.getCareSeq(), careDto.getWorkType()));
                    break;
                case CARE_LEVEL:
                    resultDto.setCareLevelList(careRepository.findByCareIdList(careTopEnum.getCareSeq(), careDto.getCareLevel()));
                    break;
                case DEMENTIA_SYMPTOM:
                    resultDto.setDementiaSymptomList(careRepository.findByCareIdList(careTopEnum.getCareSeq(), careDto.getDementiaSymptom()));
                    break;
                case INMATE_STATE:
                    resultDto.setInmateStateList(careRepository.findByCareIdList(careTopEnum.getCareSeq(), careDto.getInmateState()));
                    break;
                case GENDER:
                    resultDto.setGenderList(careRepository.findByCareIdList(careTopEnum.getCareSeq(), careDto.getGender()));
                    break;
                case SERVICE_MEAL:
                    resultDto.setServiceMealList(careRepository.findByCareIdList(careTopEnum.getCareSeq(), careDto.getServiceMeal()));
                    break;
                case SERVICE_TOILET:
                    resultDto.setServiceToiletList(careRepository.findByCareIdList(careTopEnum.getCareSeq(), careDto.getServiceToilet()));
                    break;
                case SERVICE_MOBILITY:
                    resultDto.setServiceMobilityList(careRepository.findByCareIdList(careTopEnum.getCareSeq(), careDto.getServiceMobility()));
                    break;
                case SERVICE_DAILY:
                    resultDto.setServiceDailyList(careRepository.findByCareIdList(careTopEnum.getCareSeq(), careDto.getServiceDaily()));
                    break;
                case WELFARE:
                    if(needWelfare) resultDto.setWelfareList(careRepository.findByCareIdList(careTopEnum.getCareSeq(), careDto.getWelfare()));
                    break;
            }
        }
        return resultDto;
    }

    private List<TblCareDto> getCareDtoList(TblCareTopEnum careTopEnum){
        return careRepository.findByCare_IdAndCareYnTrue(careTopEnum.getCareSeq()).stream().map(TblCareDto::new).toList();
    }
    @Override
    public CareResponseDto.GetAllCodeList getAllCodeList() {
        return CareResponseDto.GetAllCodeList.builder()
                .workTypeList(getCareDtoList(TblCareTopEnum.WORK_TYPE))                     // 근무종류
                .welfareList(getCareDtoList(TblCareTopEnum.WELFARE))                        // 복리후생
                .careLevelList(getCareDtoList(TblCareTopEnum.CARE_LEVEL))                   // 장기요양등급
                .dementiaSymptomList(getCareDtoList(TblCareTopEnum.DEMENTIA_SYMPTOM))       // 치매증상
                .inmateStateList(getCareDtoList(TblCareTopEnum.INMATE_STATE))               // 동거인 여부
                .serviceMealList(getCareDtoList(TblCareTopEnum.SERVICE_MEAL))               // 어르신 필요 서비스 - 식사보조
                .serviceToiletList(getCareDtoList(TblCareTopEnum.SERVICE_TOILET))           // 어르신 필요 서비스 - 배변보조
                .serviceMobilityList(getCareDtoList(TblCareTopEnum.SERVICE_MOBILITY))       // 어르신 필요 서비스 - 이동보조
                .serviceDailyList(getCareDtoList(TblCareTopEnum.SERVICE_DAILY))             // 어르신 필요 서비스 - 일상생활
                .gender(getCareDtoList(TblCareTopEnum.GENDER))                              // 성별
                .build();
    }
    @Override
    public CareResponseDto.GetServiceCodeList getServiceCodeList() {
        return CareResponseDto.GetServiceCodeList.builder()
                .serviceMealList(getCareDtoList(TblCareTopEnum.SERVICE_MEAL))               // 어르신 필요 서비스 - 식사보조
                .serviceToiletList(getCareDtoList(TblCareTopEnum.SERVICE_TOILET))           // 어르신 필요 서비스 - 배변보조
                .serviceMobilityList(getCareDtoList(TblCareTopEnum.SERVICE_MOBILITY))       // 어르신 필요 서비스 - 이동보조
                .serviceDailyList(getCareDtoList(TblCareTopEnum.SERVICE_DAILY))             // 어르신 필요 서비스 - 일상생활
                .build();
    }
    @Override
    public CareResponseDto.GetRequestCodeList getRequestCodeList(CareRequestDto.GetRequestCodeList req) {
        // EnumMap을 사용하여 각 Enum 값에 해당하는 setter 매핑
        Map<TblCareTopEnum, BiConsumer<CareResponseDto.GetRequestCodeList, List<TblCareDto>>> setterMap = new EnumMap<>(TblCareTopEnum.class);
        setterMap.put(TblCareTopEnum.WORK_TYPE, CareResponseDto.GetRequestCodeList::setWorkTypeList);
        setterMap.put(TblCareTopEnum.WELFARE, CareResponseDto.GetRequestCodeList::setWelfareList);
        setterMap.put(TblCareTopEnum.CARE_LEVEL, CareResponseDto.GetRequestCodeList::setCareLevelList);
        setterMap.put(TblCareTopEnum.DEMENTIA_SYMPTOM, CareResponseDto.GetRequestCodeList::setDementiaSymptomList);
        setterMap.put(TblCareTopEnum.INMATE_STATE, CareResponseDto.GetRequestCodeList::setInmateStateList);
        setterMap.put(TblCareTopEnum.SERVICE, CareResponseDto.GetRequestCodeList::setServiceList);
        setterMap.put(TblCareTopEnum.SERVICE_MEAL, CareResponseDto.GetRequestCodeList::setServiceMealList);
        setterMap.put(TblCareTopEnum.SERVICE_TOILET, CareResponseDto.GetRequestCodeList::setServiceToiletList);
        setterMap.put(TblCareTopEnum.SERVICE_MOBILITY, CareResponseDto.GetRequestCodeList::setServiceMobilityList);
        setterMap.put(TblCareTopEnum.SERVICE_DAILY, CareResponseDto.GetRequestCodeList::setServiceDailyList);
        setterMap.put(TblCareTopEnum.GENDER, CareResponseDto.GetRequestCodeList::setGender);

        // 데이터를 가져오는 공통 로직
        Function<TblCareTopEnum, List<TblCareDto>> fetchCareList = this::getCareDtoList;

        // 요청된 Enum 리스트를 순회하며 동적으로 처리
        CareResponseDto.GetRequestCodeList resultDto = new CareResponseDto.GetRequestCodeList();
        for (TblCareTopEnum topEnum : req.getCareTopEnumList()) {
            setterMap.getOrDefault(topEnum, (dto, list) -> {}).accept(resultDto, fetchCareList.apply(topEnum));
        }

        return resultDto;
    }
}
