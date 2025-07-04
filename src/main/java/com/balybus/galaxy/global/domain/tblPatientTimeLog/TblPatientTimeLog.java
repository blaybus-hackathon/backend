package com.balybus.galaxy.global.domain.tblPatientTimeLog;

import com.balybus.galaxy.global.domain.BaseEntity;
import com.balybus.galaxy.global.domain.tblPatientLog.TblPatientLog;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalTime;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class TblPatientTimeLog extends BaseEntity {

    @Id
    @Column(name = "ptl_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("돌봄시간 구분자")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pl_seq" , nullable = false)
    @Comment("어르신 정보 구분자")
    private TblPatientLog patientLog;

    @Comment("요일")
    private int ptlDate;

    @Comment("시작시간")
    private LocalTime ptlStartTime;

    @Comment("종료시간")
    private LocalTime ptlEndTime;

}
