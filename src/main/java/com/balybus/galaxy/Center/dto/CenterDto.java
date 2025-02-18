package com.balybus.galaxy.Center.dto;

import com.balybus.galaxy.Center.domain.TblSignUpCenter;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CenterDto {
    private Long id;
    private Long imgSeq;
    private String name;
    private String tel;
    private Boolean carYn;
    private String address;
    private Character grade;
    private LocalDateTime openDate;
    private String introduce;
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;

    public static CenterDto fromEntity(TblSignUpCenter center) {
        return CenterDto.builder()
                .id(center.getId())
                .imgSeq(center.getImgSeq())
                .name(center.getName())
                .tel(center.getTel())
                .carYn(center.getCarYn())
                .address(center.getAddress())
                .grade(center.getGrade())
                .openDate(center.getOpenDate())
                .introduce(center.getIntroduce())
                .createDatetime(center.getCreateDatetime())
                .updateDatetime(center.getUpdateDatetime())
                .build();
    }

    public TblSignUpCenter toEntity() {
        return TblSignUpCenter.builder()
                .imgSeq(this.imgSeq)
                .name(this.name)
                .tel(this.tel)
                .carYn(this.carYn)
                .address(this.address)
                .grade(this.grade)
                .openDate(this.openDate)
                .introduce(this.introduce)
                .build();
    }
}
