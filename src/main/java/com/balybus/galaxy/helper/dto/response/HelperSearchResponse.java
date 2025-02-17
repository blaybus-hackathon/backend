package com.balybus.galaxy.helper.dto.response;

import com.balybus.galaxy.helper.domain.TblHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HelperSearchResponse {
    private List<TblHelper> helpers;
}
