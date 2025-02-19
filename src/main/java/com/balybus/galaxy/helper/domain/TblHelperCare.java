package com.balybus.galaxy.helper.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TblHelperCare {

    @Id
    @Column(name = "he_seq")
    @Comment("경력 사항 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "helper_seq")
    @Comment("요양 보호사 정보 구분자")
    private TblHelper helper;

    @Column(name = "care_seq_care_level", nullable = false)
    @Comment("장기요양등급(TblCare)")
    private int careLevel;

    @Column(name = "care_seq_inmate_state", nullable = false)
    @Comment("동거인 여부(TblCare)")
    private int inmateState;

    @Comment("근무종류(TblCare)")
    @Column(name = "care_seq_work_type", nullable = false)
    private int workType;

    @Comment("성별(TblCare)")
    @Column(name = "care_seq_gender", nullable = false)
    private int gender; //남성:1 여성:2

    @Comment("복리후생(TblCare)")
    @Column(name = "care_seq_welfare", nullable = false)
    private int welfare;

    @Comment("치매증상(TblCare)")
    @Column(name = "care_seq_dementia_symptom", nullable = false)
    private int dementiaSymptom;

    @Comment("식사보조(TblCare)")
    @Column(name = "care_seq_service_meal", nullable = false)
    private int serviceMeal;

    @Comment("배변보조(TblCare)")
    @Column(name = "care_seq_service_toilet", nullable = false)
    private int serviceToilet;

    @Comment("이동보조(TblCare)")
    @Column(name = "care_seq_service_mobility", nullable = false)
    private int serviceMobility;

    @Comment("일상생활(TblCare)")
    @Column(name = "care_seq_service_daily", nullable = false)
    private int serviceDaily;
}
