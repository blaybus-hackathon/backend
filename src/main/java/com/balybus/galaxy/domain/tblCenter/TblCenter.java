package com.balybus.galaxy.domain.tblCenter;

import com.balybus.galaxy.domain.BaseEntity;
import com.balybus.galaxy.domain.tblCenter.dto.CenterRequestDto;
import com.balybus.galaxy.domain.tblImg.TblImg;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TblCenter extends BaseEntity {
    @Id
    @Column(name="center_seq")
    @Comment(value="센터 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            //구분자

//    @OneToOne(fetch = FetchType.LAZY)
//    @Comment("이미지 구분자")
//    @JoinColumn(name = "img_seq")
//    private TblImg img;   // 센터 이미지는 없다.

    @Comment(value="센터코드")
    @Column(length = 6, nullable = false, unique = true)
    private String centerCode;          //센터코드

    @Comment(value="센터명")
    @Column(length = 50, nullable = false)
    private String centerName;          //센터명

    @Comment(value="연락처")
    @Column(length = 11, nullable = false)
    private String centerTel;           //연락처

    @Comment(value="센터 목용차량 소유 여부")
    @Column(nullable = false)
    private boolean centerCarYn;        //센터 목용차량 소유 여부

    @Comment(value="센터 주소")
    @Column(length = 100, nullable = false)
    private String centerAddress;       //센터 주소

    @Comment(value="센터등급")
    @Column(length = 1)
    private String centerGrade;         //센터등급

    @Comment(value="센터 운영 시작일")
    private LocalDate centerOpenDate;   //센터 운영 시작일

    @Comment(value="한줄 소개")
    @Column(length = 100)
    private String centerIntroduce;     //한줄 소개


    /* ==================================================
     * UPDATE
     * ================================================== */
    public void updateCenter(CenterRequestDto.UpdateCenter dto){
        this.centerName = dto.getName();
        this.centerTel = dto.getTel();
        this.centerCarYn = dto.getCarYn();
        this.centerAddress = dto.getAddress();
        this.centerGrade = dto.getGrade();
        this.centerOpenDate = dto.getOpenDate();
        this.centerIntroduce = dto.getIntroduce();
    }
}