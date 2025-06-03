package com.balybus.galaxy.patient.domain.tblPatientTime;

import com.balybus.galaxy.global.domain.BaseEntity;
import com.balybus.galaxy.patient.domain.tblPatient.TblPatient;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class TblPatientTime extends BaseEntity {
    @Id
    @Column(name = "pt_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("돌봄시간 구분자")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_seq" , nullable = false)
    @Comment("어르신 정보 구분자")
    private TblPatient patient;

    @Comment("요일")
    private int ptDate;

    @Comment("시작시간")
    private LocalTime ptStartTime;

    @Comment("종료시간")
    private LocalTime ptEndTime;

}
