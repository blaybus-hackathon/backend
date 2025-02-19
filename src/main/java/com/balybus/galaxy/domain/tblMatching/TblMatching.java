package com.balybus.galaxy.domain.tblMatching;

import com.balybus.galaxy.domain.BaseEntity;
import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLog;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TblMatching extends BaseEntity {

    @Id
    @Column(name="match_seq")
    @Comment(value="매칭 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // 매칭 구분자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pl_seq")
    @Comment("어르신 공고 구분자")
    private TblPatientLog patientLog;           // 어르신 공고 구분자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helper_seq")
    @Comment("요양보호사 구분자")
    private TblHelper helper;           // 요양보호사 구분자

    @Comment("최종 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double totalScore;

    @Comment("근무지 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double locationScore;

    @Comment("근무시간 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double timeScore;

    @Comment("근무요일 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double dateScore;

    @Comment("근무종류 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double workType;

    @Comment("복지 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double welfare;

    @Comment("장기요양등급 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double careLevel;

    @Comment("치매증상 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double dementiaSymptom;

    @Comment("동거인 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double inmateState;

    @Comment("성별 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double gender;

    @Comment("식사보조 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double serviceMeal;

    @Comment("이동보조 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double serviceMobility;

    @Comment("배변보조 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double serviceToilet;

    @Comment("일상보조 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double serviceDaily;

    @Comment("경력 점수")
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer helperExp;

    @Comment("자격증 점수")
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private Double certScore;

    @Comment("급여 점수")
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer wageScore;

    @Comment("매칭상태")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchState matchState;

    @Comment("사용여부")
    private boolean useYn;

    /* ===============================================================
     * UPDATE
     * ===============================================================*/
    public void useNo(){
        this.useYn = false;
    }
}
