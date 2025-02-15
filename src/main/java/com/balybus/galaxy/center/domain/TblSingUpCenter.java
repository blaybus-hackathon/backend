package com.balybus.galaxy.center.domain;

import com.balybus.galaxy.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Optional;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class TblSingUpCenter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "center_seq")
    @Comment("센터 구분자")
    private Long id;

    @Column(name = "img_seq")
    @Comment("이미지 구분자")
    private Long imgSeq;

    @Column(name = "center_name", length = 50, nullable = false)
    @Comment("센터명")
    private String centerName;

    @Column(name = "center_tel", length = 11, nullable = false)
    @Comment("연락처")
    private String centerTel;

    @Column(name = "center_car_yn", nullable = false)
    @Comment("목욕차량 소유 여부")
    private Boolean centerCarYn;

    @Column(name = "center_address", length = 100, nullable = false)
    @Comment("센터 주소")
    private String centerAddress;

    @Column(name = "center_grade", length = 1)
    @Comment("센터등급")
    private String centerGrade;

    @Column(name = "center_open_date")
    @Comment("센터 운영 시작일")
    private LocalDateTime centerOpenDate;

    @Column(name = "center_introduce", length = 100)
    @Comment("한줄 소개")
    private String centerIntroduce;

    @Column(name = "create_datetime", nullable = false, updatable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;

    @Column(name = "update_datetime", nullable = false)
    @Comment("수정일시")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = Optional.ofNullable(createdAt).orElse(LocalDateTime.now());
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}