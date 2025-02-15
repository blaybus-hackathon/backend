package com.balybus.galaxy.patient.domain;

import com.balybus.galaxy.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class TblPatientadress extends BaseEntity {

    @Column(name = "af_seq")
    @Comment("시.도 구분자")
    private String tblPatientFirst;

    @Column(name = "as_seq")
    @Comment("시.군.구 구분자")
    private String tblPatientSecond;

    @Column(name = "at_seq")
    @Comment("읍.면.동 구분자")
    private String tblPatientThrid;



}
