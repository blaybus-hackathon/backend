package com.balybus.galaxy.helper.dto.response;

import com.balybus.galaxy.helper.domain.TblHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HelperSearchResponse {
    private List<TblHelper> helpers;
    List<HelperSearchInfo> helperSearchInfos;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static public class HelperSearchInfo {
        private String email;
        private String name;
        private String gender;
        private String age;
        private String experience;
        private String workTerm;
        private String introduce;
    }
}
