package com.balybus.galaxy.patient.domain.tblPatientLog;

import com.balybus.galaxy.address.domain.TblAddressFirst;
import com.balybus.galaxy.address.domain.TblAddressSecond;
import com.balybus.galaxy.address.domain.TblAddressThird;
import com.balybus.galaxy.domain.BaseEntity;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.patient.domain.tblPatient.TblPatient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class TblPatientLog extends BaseEntity {

    @Id
    @Column(name = "pl_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("어르신 공고 구분자")
    private Long id;

    @Comment("기본정보 업데이트 여부")
    @Column(name = "pl_linking_yn", nullable = false)
    private boolean linkingYn;

    @Comment("복리후생(TblCare)")
    @Column(name = "care_seq_welfare", nullable = false)
    private int welfare;

    @Column(name = "pl_wage_negotiation")
    @Comment("급여 협의 여부")
    private Boolean wageNegotiation;

    @Column(name = "pl_wage_state")
    @Comment("1:시급, 2:일급, 3:주급 구분")
    private int wageState;

    @Column(name = "pl_wage")
    @Comment("급여")
    private int wage;

    @Column(name = "pl_time_wage")
    @Comment("시급")
    private double timeWage;

    @Column(name = "pl_day_wage")
    @Comment("일급")
    private double dayWage;

    @Column(name = "pl_week_wage")
    @Comment("주급")
    private double weekWage;

    @Column(name = "pl_request_contents", length = 255)
    @Comment("기타 요청 사항")
    private String requestContents;




    // TblPatient 데이블 공통 사항
    @ManyToOne
    @JoinColumn(name = "patient_seq", nullable = false)
    @Comment("어르신 구분자")
    private TblPatient patient;

    @ManyToOne
    @JoinColumn(name = "manager_seq", nullable = false)
    @Comment("관리자 구분자")
    private TblCenterManager manager;

    @ManyToOne
    @NotNull
    @Comment("시.도 구분자")
    @JoinColumn(name = "af_seq", nullable = false)
    private TblAddressFirst tblAddressFirst;

    @ManyToOne
    @NotNull
    @Comment("시.군.구 구분자")
    @JoinColumn(name = "as_seq", nullable = false)
    private TblAddressSecond tblAddressSecond;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "at_seq", nullable = false)
    @Comment("읍.면.동 구분자")
    private TblAddressThird tblAddressThird;



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



    @Column(name = "pl_name", nullable = false, length = 100)
    @Comment("어르신 이름")
    private String name;

    @Column(name = "pl_birth", nullable = false)
    @Comment("생년월일")
    private String birthDate;

    @Column(name = "pl_weight")
    @Comment("몸무게")
    private Double weight;

    @Column(name = "pl_diseases", length = 255)
    @Comment("보유 질병/질환")
    private String diseases;

    @Column(name = "pl_time_negotiation")
    @Comment("돌봄 요일 시간 협의 여부")
    private Boolean timeNegotiation;
}
