package com.balybus.galaxy.global.utils.mail.dto.contents;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MailMatchingDto {
    private String plName;  // 어르신 성함
    private List<HelperContentDto> contentList;

    @Getter
    @Builder
    public static class HelperContentDto {
        private String name; //요양보호사 이름
        private String gender; //성별
        private int age; //나이
    }
}
