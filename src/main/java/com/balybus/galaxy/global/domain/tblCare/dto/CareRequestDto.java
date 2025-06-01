package com.balybus.galaxy.global.domain.tblCare.dto;

import com.balybus.galaxy.global.domain.tblCare.TblCareTopEnum;
import lombok.Data;

import java.util.List;

public class CareRequestDto {
    @Data
    public static class GetRequestCodeList{
        private List<TblCareTopEnum> careTopEnumList;
    }
}
