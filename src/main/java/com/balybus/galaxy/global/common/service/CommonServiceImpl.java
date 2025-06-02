package com.balybus.galaxy.global.common.service;

import com.balybus.galaxy.global.domain.tblCare.TblCareRepository;
import com.balybus.galaxy.global.domain.tblCare.TblCareTopEnum;
import com.balybus.galaxy.global.domain.tblCare.dto.CareBaseDto;
import com.balybus.galaxy.global.domain.tblCare.dto.CareChoiceListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {
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
}
