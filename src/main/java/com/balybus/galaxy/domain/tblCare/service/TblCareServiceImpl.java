package com.balybus.galaxy.domain.tblCare.service;

import com.balybus.galaxy.domain.tblCare.TblCareRepository;
import com.balybus.galaxy.domain.tblCare.TblCareTopEnum;
import com.balybus.galaxy.domain.tblCare.dto.CareRequestDto;
import com.balybus.galaxy.domain.tblCare.dto.CareResponseDto;
import com.balybus.galaxy.domain.tblCare.dto.TblCareDto;
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

    @Override
    public CareResponseDto.GetAllCodeList getAllCodeList() {
        return CareResponseDto.GetAllCodeList.builder()
                .workTypeList(careRepository.findByCare_IdAndCareYnTrue(TblCareTopEnum.WORK_TYPE.getCareSeq()).stream().map(TblCareDto::new).toList())          // 근무종류
                .welfareList(careRepository.findByCare_IdAndCareYnTrue(TblCareTopEnum.WELFARE.getCareSeq()).stream().map(TblCareDto::new).toList())           // 복리후생
                .careLevelList(careRepository.findByCare_IdAndCareYnTrue(TblCareTopEnum.CARE_LEVEL.getCareSeq()).stream().map(TblCareDto::new).toList())         // 장기요양등급
                .dementiaSymptomList(careRepository.findByCare_IdAndCareYnTrue(TblCareTopEnum.DEMENTIA_SYMPTOM.getCareSeq()).stream().map(TblCareDto::new).toList())   // 치매증상
                .inmateStateList(careRepository.findByCare_IdAndCareYnTrue(TblCareTopEnum.INMATE_STATE.getCareSeq()).stream().map(TblCareDto::new).toList())       // 동거인 여부
                .serviceMealList(careRepository.findByCare_IdAndCareYnTrue(TblCareTopEnum.SERVICE_MEAL.getCareSeq()).stream().map(TblCareDto::new).toList())       // 어르신 필요 서비스 - 식사보조
                .serviceToiletList(careRepository.findByCare_IdAndCareYnTrue(TblCareTopEnum.SERVICE_TOILET.getCareSeq()).stream().map(TblCareDto::new).toList())     // 어르신 필요 서비스 - 배변보조
                .serviceMobilityList(careRepository.findByCare_IdAndCareYnTrue(TblCareTopEnum.SERVICE_MOBILITY.getCareSeq()).stream().map(TblCareDto::new).toList())   // 어르신 필요 서비스 - 이동보조
                .serviceDailyList(careRepository.findByCare_IdAndCareYnTrue(TblCareTopEnum.SERVICE_DAILY.getCareSeq()).stream().map(TblCareDto::new).toList())      // 어르신 필요 서비스 - 일상생활
                .gender(careRepository.findByCare_IdAndCareYnTrue(TblCareTopEnum.GENDER.getCareSeq()).stream().map(TblCareDto::new).toList())      // 성별
                .build();
    }
    @Override
    public CareResponseDto.GetServiceCodeList getServiceCodeList() {
        return CareResponseDto.GetServiceCodeList.builder()
                .serviceMealList(careRepository.findByCare_IdAndCareYnTrue(TblCareTopEnum.SERVICE_MEAL.getCareSeq()).stream().map(TblCareDto::new).toList())       // 어르신 필요 서비스 - 식사보조
                .serviceToiletList(careRepository.findByCare_IdAndCareYnTrue(TblCareTopEnum.SERVICE_TOILET.getCareSeq()).stream().map(TblCareDto::new).toList())     // 어르신 필요 서비스 - 배변보조
                .serviceMobilityList(careRepository.findByCare_IdAndCareYnTrue(TblCareTopEnum.SERVICE_MOBILITY.getCareSeq()).stream().map(TblCareDto::new).toList())   // 어르신 필요 서비스 - 이동보조
                .serviceDailyList(careRepository.findByCare_IdAndCareYnTrue(TblCareTopEnum.SERVICE_DAILY.getCareSeq()).stream().map(TblCareDto::new).toList())      // 어르신 필요 서비스 - 일상생활
                .build();
    }

    @Override
    public CareResponseDto.GetRequestCodeList getRequestCodeList(CareRequestDto.GetRequestCodeList req) {
        CareResponseDto.GetRequestCodeList resultDto = new CareResponseDto.GetRequestCodeList();

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
        Function<TblCareTopEnum, List<TblCareDto>> fetchCareList =
                e -> careRepository.findByCare_IdAndCareYnTrue(e.getCareSeq()).stream()
                        .map(TblCareDto::new)
                        .toList();

        // 요청된 Enum 리스트를 순회하며 동적으로 처리
        for (TblCareTopEnum topEnum : req.getCareTopEnumList()) {
            setterMap.getOrDefault(topEnum, (dto, list) -> {}).accept(resultDto, fetchCareList.apply(topEnum));
        }

        return resultDto;
    }

    public List<Long> getCareChoiceList(TblCareTopEnum topEnum, int calValTotal){
        return careRepository.findByCareIdList(topEnum.getCareSeq(), calValTotal);
    }

//    public List<Long> getCareChoiceList(TblCareTopEnum topEnum, int calValTotal){
//        TblCareTopEnum[] careTopEnumList = TblCareTopEnum.values();
//        for(TblCareTopEnum careTopEnum : careTopEnumList) {
//            switch (careTopEnum) {
//                case WORK_TYPE:
//                    resultDto.setWorkTypeList(careRepository.findByCareIdList(careTopEnum, resultDto.getWorkType()));
//                    break;
//                case CARE_LEVEL:
//                    resultDto.setCareLevelList(careRepository.findByCareIdList(careTopEnum, resultDto.getCareLevel()));
//                    break;
//                case DEMENTIA_SYMPTOM:
//                    resultDto.setDementiaSymptomList(careRepository.findByCareIdList(careTopEnum, resultDto.getDementiaSymptom()));
//                    break;
//                case INMATE_STATE:
//                    resultDto.setInmateStateList(careRepository.findByCareIdList(careTopEnum, resultDto.getInmateState()));
//                    break;
//                case GENDER:
//                    resultDto.setGenderList(careRepository.findByCareIdList(careTopEnum, resultDto.getGender()));
//                    break;
//                case SERVICE_MEAL:
//                    resultDto.setServiceMealList(careRepository.findByCareIdList(careTopEnum, resultDto.getServiceMeal()));
//                    break;
//                case SERVICE_TOILET:
//                    resultDto.setServiceToiletList(careRepository.findByCareIdList(careTopEnum, resultDto.getServiceToilet()));
//                    break;
//                case SERVICE_MOBILITY:
//                    resultDto.setServiceMobilityList(careRepository.findByCareIdList(careTopEnum, resultDto.getServiceMobility()));
//                    break;
//                case SERVICE_DAILY:
//                    resultDto.setServiceDailyList(careRepository.findByCareIdList(careTopEnum, resultDto.getServiceDaily()));
//                    break;
//            }
//        }
//        return careRepository.findByCareIdList(topEnum.getCareSeq(), calValTotal);
//    }
}
