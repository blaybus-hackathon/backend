package com.balybus.galaxy.Center.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_CENTER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TblSignUpCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CENTER_SEQ")
    private Long id;

    @Column(name = "IMG_SEQ")
    private Long imgSeq;

    @Column(name = "CENTER_NAME", length = 50, nullable = false)
    private String name;

    @Column(name = "CENTER_TEL", length = 11, nullable = false)
    private String tel;

    @Column(name = "CENTER_CAR_YN", nullable = false)
    private Boolean carYn;

    @Column(name = "CENTER_ADDRESS", length = 100, nullable = false)
    private String address;

    @Column(name = "CENTER_GRADE", length = 1)
    private Character grade;

    @Column(name = "CENTER_OPEN_DATE")
    private LocalDateTime openDate;

    @Column(name = "CENTER_INTRODUCE", length = 100)
    private String introduce;

    @Column(name = "CREATE_DATETIME", updatable = false)
    @Builder.Default
    private LocalDateTime createDatetime = LocalDateTime.now();

    @Column(name = "UPDATE_DATETIME")
    @Builder.Default
    private LocalDateTime updateDatetime = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        this.createDatetime = LocalDateTime.now();
        this.updateDatetime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDatetime = LocalDateTime.now();
    }
}
