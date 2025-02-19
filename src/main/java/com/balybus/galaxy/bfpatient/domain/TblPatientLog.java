//package com.balybus.galaxy.bfpatient.domain;
//
//import com.balybus.galaxy.address.domain.TblAddressFirst;
//import com.balybus.galaxy.address.domain.TblAddressSecond;
//import com.balybus.galaxy.address.domain.TblAddressThird;
//import com.balybus.galaxy.domain.BaseEntity;
//import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import org.hibernate.annotations.Comment;
//
//public class TblPatientLog extends BaseEntity {
//
//    /*fk */
//    @ManyToOne
//    @JoinColumn(name = "manager_seq", nullable = false)
//    @Comment("관리자 구분자")
//    private TblCenterManager manager;
//
//    @Id
//    @Column(name = "patient_seq")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Comment("어르신 구분자")
//    private Long id;
//
//    @Column(name = "long_term_care_grade_seq", nullable = false)
//    @Comment("어르신 장기요양등급 구분자")
//    private Long longTermCareGradeSeq;
//
//    @Column(name = "guardian_info_seq", nullable = false)
//    @Comment("보호자 정보 구분자")
//    private Long guardianInfoSeq;
//
//    @Comment("시.도 구분자")
//    @ManyToOne
//    @NotNull
//    @JoinColumn(name = "af_seq", nullable = false)
//    private TblAddressFirst tblAddressFirst;
//
//    @Comment("시.군.구 구분자")
//    @ManyToOne
//    @JoinColumn(name = "as_seq", nullable = false)
//    @NotNull
//    private TblAddressSecond tblAddressSecond;
//
//    @Comment("읍.면.동 구분자")
//    @ManyToOne
//    @JoinColumn(name = "at_seq", nullable = false)
//    @NotNull
//    private TblAddressThird tblAddressThird;
//
//    @Column(name = "patient_name", nullable = false, length = 100)
//    @Comment("어르신 이름")
//    private String name;
//
//    @Column(name = "patient_birth_date", nullable = false)
//    @Comment("생년월일")
//    private String birthDate;
//
//    @Column(name = "patient_gender", length = 10)
//    @Comment("성별")
//    private String gender;
//
//    @Column(name = "patient_weight")
//    @Comment("몸무게")
//    private Double weight;
//
//    @Column(name = "patient_diseases", length = 255)
//    @Comment("보유 질병/질환")
//    private String diseases;
//
//    @Column(name = "patient_start_time")
//    @Comment("시작시간")
//    @NotNull
//    private String startTime;
//
//    @Column(name = "patient_end_time")
//    @Comment("종료시간")
//    @NotNull
//    private String endTime;
//}
