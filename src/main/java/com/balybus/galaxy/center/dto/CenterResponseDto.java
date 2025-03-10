package com.balybus.galaxy.center.dto;

import com.balybus.galaxy.center.entity.CenterEntity;
import lombok.Getter;

@Getter
public class CenterResponseDto {
    private Long id;
    private String issueCode;
    private String name;
    private String address;
    private String description;

    // Entity → DTO 변환 생성자
    public CenterResponseDto(CenterEntity center) {
        this.id = center.getId();
        this.issueCode = center.getIssueCode();
        this.name = center.getName();
        this.address = center.getAddress();
        this.description = center.getDescription();
    }
}