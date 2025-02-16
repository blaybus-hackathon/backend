//package com.balybus.galaxy.patient.domain;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.Comment;
//
//import java.time.LocalDateTime;
//
//@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@Builder
//@Getter
//public class TblPatient {
//
//    @Id
//    @Column(name = "patient_seq")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Comment("어르신 구분자")
//    private Long id;
//
//    @Column(name = "manager_seq", nullable = false)
//    @Comment("관리자 구분자")
//    private Long managerSeq;
//
//    @Column(name = "long_term_care_grade_seq", nullable = false)
//    @Comment("어르신 장기요양등급 구분자")
//    private Long longTermCareGradeSeq;
//
//    @Column(name = "guardian_info_seq", nullable = false)
//    @Comment("보호자 정보 구분자")
//    private Long guardianInfoSeq;
//
//    @Column(name = "state_code", nullable = false)
//    @Comment("시도 구분자 (주소)")
//    private String tblPatientFirst;
//
//    @Column(name = "city_code", nullable = false)
//    @Comment("시군구 구분자 (주소)")
//    private String tblPatientSecond;
//
//    @Column(name = "district_code", nullable = false)
//    @Comment("읍면동 구분자 (주소)")
//    private String tblPatientThrid;
//
//    @Column(name = "name", nullable = false, length = 100)
//    @Comment("어르신 이름")
//    private String name;
//
//    @Column(name = "birth_date", nullable = false)
//    @Comment("생년월일")
//    private String birthDate;
//
//    @Column(name = "gender", length = 10)
//    @Comment("성별")
//    private String gender;
//
//    @Column(name = "weight")
//    @Comment("몸무게")
//    private Double weight;
//
//    @Column(name = "diseases", length = 255)
//    @Comment("보유 질병/질환")
//    private String diseases;
//
//    @Column(name = "created_at", nullable = false)
//    @Comment("생성일시")
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at", nullable = false)
//    @Comment("수정일시")
//    private LocalDateTime updatedAt;
//
//    @PrePersist
//    public void prePersist() {
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }
//}
