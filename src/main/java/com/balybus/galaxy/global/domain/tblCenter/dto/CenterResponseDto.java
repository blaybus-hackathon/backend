package com.balybus.galaxy.global.domain.tblCenter.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class CenterResponseDto {
    @Getter
    @Builder
    public static class GetCenterList {
        private int totalPage;                  // 전페 페이지 개수
        private long totalEle;                  // 전체 리스트 개수
        private boolean hasNext;                // 다음 페이지 존재 여부
        private List<GetCenterListInfo> list;  // 리스트
    }
    @Getter
    @Builder
    public static class GetCenterListInfo {
        private Long centerSeq;
        private String centerName;        // 센터 이름
        private String centerAddress;     // 주소
    }

    @Getter
    @Builder
    public static class RegisterCenter {
        private Long centerSeq;
    }
}
