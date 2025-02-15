package com.balybus.galaxy.helper.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HelperWorkLocationDTO {
    private Long HelperId;
    private List<Long> addressFirstIds; // 시.도
    private List<Long> addressSecondIds; // 시.군.구
    private List<Long> addressThirdIds; // 읍.면.동
}