package com.balybus.galaxy.domain.tblCare.dto;

import com.balybus.galaxy.domain.tblCare.TblCareTopEnum;
import lombok.Data;

import java.util.List;

public class CareRequestDto {
    @Data
    public static class GetRequestCodeList{
        private List<TblCareTopEnum> careTopEnumList;
    }
}
