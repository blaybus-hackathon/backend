package com.balybus.galaxy.patient.domain;

import com.balybus.galaxy.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TblPatientTime extends BaseEntity {

    /*fk*/
    @ManyToOne
    @JoinColumn(name = "pt_seq" , nullable = false)
    @Comment("근무가능시간 구분자")
    private TblPatientTime pt_seq;

    /*pk*/
    @Id
    @Column(name = "patient_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("어르신 구분자")
    private Long id;

    @Column(name="pt_date")
    @Comment("요일")
    private int pt_date;

    @Column(name="pt_start_time")
    @Comment("시작시간")
    private LocalTime pt_start_time;

    @Column(name="pt_end_time")
    @Comment("종료시간")
    private LocalTime pt_end_time;




}
