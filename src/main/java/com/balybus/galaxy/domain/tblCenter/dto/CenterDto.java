package com.balybus.galaxy.domain.tblCenter.dto;

import com.balybus.galaxy.domain.tblCenter.TblCenter;
import com.balybus.galaxy.domain.tblImg.TblImg;
import lombok.*;

import java.time.LocalDate;
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
    private String grade;
    private LocalDate openDate;
    private String introduce;
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;

    public static CenterDto fromEntity(TblCenter center) {
        return CenterDto.builder()
                .id(center.getId())
                .imgSeq(center.getImg() == null ? null : center.getImg().getId())
                .name(center.getCenterName())
                .tel(center.getCenterTel())
                .carYn(center.isCenterCarYn())
                .address(center.getCenterAddress())
                .grade(center.getCenterGrade())
                .openDate(center.getCenterOpenDate())
                .introduce(center.getCenterIntroduce())
                .createDatetime(center.getCreateDatetime())
                .updateDatetime(center.getUpdateDatetime())
                .build();
    }

    public TblCenter toEntity() {
        return TblCenter.builder()
                .img(this.imgSeq == null ? null : TblImg.builder().id(this.imgSeq).build())
                .centerName(this.name)
                .centerTel(this.tel)
                .centerCarYn(this.carYn)
                .centerAddress(this.address)
                .centerGrade(this.grade)
                .centerOpenDate(this.openDate)
                .centerIntroduce(this.introduce)
                .build();
    }
}
