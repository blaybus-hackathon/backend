package com.balybus.galaxy.patient.domain.tblPatient;

import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;
import com.balybus.galaxy.global.domain.tblAddressSecond.TblAddressSecond;
import com.balybus.galaxy.global.domain.tblAddressThird.TblAddressThird;
import com.balybus.galaxy.global.domain.BaseEntity;
import com.balybus.galaxy.global.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.global.domain.tblImg.TblImg;
import com.balybus.galaxy.global.utils.file.ChangeProfileImg;
import com.balybus.galaxy.patient.basic.dto.BasicRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class TblPatient extends BaseEntity implements ChangeProfileImg {
        @Id
        @Column(name = "patient_seq")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Comment("어르신 구분자")
        private Long id;

        @ManyToOne
        @JoinColumn(name = "manager_seq", nullable = false)
        @Comment("관리자 구분자")
        private TblCenterManager manager;

        @OneToOne(fetch = FetchType.LAZY)
        @Comment("이미지 구분자")
        @JoinColumn(name = "img_seq")
        private TblImg img;

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



        @Column(name = "patient_name", nullable = false, length = 100)
        @Comment("어르신 이름")
        private String name;

        @Column(name = "patient_birth", nullable = false)
        @Comment("생년월일(YYYYMMDD)")
        private String birthDate;

        @Column(name = "patient_weight")
        @Comment("몸무게")
        private Double weight;

        @Column(name = "patient_diseases", length = 255)
        @Comment("보유 질병/질환")
        private String diseases;

        @Column(name = "patient_time_negotiation")
        @Comment("돌봄 요일 시간 협의 여부")
        private Boolean timeNegotiation;

        /* ========================================================
         * UPDATE
         * ========================================================*/
        @Override
        public TblImg getImg() { return this.img; }

        @Override
        public void updateImg(TblImg img) { this.img = img; }

        public void basicUpdate(BasicRequestDto.UpdatePatientInfo dto, TblAddressFirst tblAddressFirst,
                                TblAddressSecond tblAddressSecond, TblAddressThird tblAddressThird){
                this.tblAddressFirst = tblAddressFirst;
                this.tblAddressSecond = tblAddressSecond;
                this.tblAddressThird = tblAddressThird;
                this.careLevel = dto.getCareLevel();
                this.inmateState = dto.getInmateState();
                this.workType = dto.getWorkType();
                this.gender = dto.getGender();
                this.dementiaSymptom = dto.getDementiaSymptom();
                this.serviceMeal = dto.getServiceMeal();
                this.serviceToilet = dto.getServiceToilet();
                this.serviceMobility = dto.getServiceMobility();
                this.serviceDaily = dto.getServiceDaily();
                this.name = dto.getName();
                this.birthDate = dto.getBirthDate();
                this.weight = dto.getWeight();
                this.diseases = dto.getDiseases();
                this.timeNegotiation = dto.getTimeNegotiation();
        }
}
