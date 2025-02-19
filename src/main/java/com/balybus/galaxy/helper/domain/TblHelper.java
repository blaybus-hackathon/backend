package com.balybus.galaxy.helper.domain;

import com.balybus.galaxy.domain.BaseEntity;
import com.balybus.galaxy.helper.util.StringListConverter;
import com.balybus.galaxy.domain.tblImg.TblImg;
import com.balybus.galaxy.global.utils.file.ChangeProfileImg;
import com.balybus.galaxy.member.domain.TblUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TblHelper extends BaseEntity implements ChangeProfileImg {

    @Id
    @Column(name = "helper_seq")
    @Comment("요양보호사 정보 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @Comment("유저 구분자")
    @JoinColumn(name = "user_seq", nullable = false)
    private TblUser user;

    @OneToOne(fetch = FetchType.LAZY)
    @Comment("이미지 구분자")
    @JoinColumn(name = "img_seq")
    private TblImg img;

    @Column(name = "helper_name", length = 10)
    @Comment("요양보호사 이름")
    private String name;

    @Column(name = "helper_tel", length = 11)
    @Comment("요양보호사 연락처")
    private String phone;

    @Column(name = "helper_gender")
    @Comment("요양보호사 성별")
    private Integer gender; // 0이면 남성, 1이면 여성

    @Column(name = "helper_birthday")
    @Comment("요양보호사 생년월일")
    private String birthday;

    @Column(name = "helper_address_detail", length = 255)
    @Comment("상세 주소")
    private String addressDetail;

    @Column(name = "helper_essential_cert_no", length = 11)
    @Comment("요양 보호사 자격증 번호")
    private String essentialCertNo;

    @Column(name = "helper_care_cert_no", length = 11)
    @Comment("간병사 자격증 번호")
    private String careCertNo;

    @Column(name = "helper_nurse_cert_no", length = 11)
    @Comment("병원 동행 매니저 자격증 번호")
    private String nurseCertNo;

    @Column(name = "helper_post_partum_cert_no", length = 11)
    @Comment("산후 관리사 자격증 번호")
    private String postPartumCertNo;

    @Column(name = "helper_other_certs", length = 500)
    @Convert(converter = StringListConverter.class)
    @Comment("기타 자격증 (간호 조무사, 간호사, 생활 지원사 등)")
    private List<String> helperOtherCerts;

    @Column(name = "helper_car_yn")
    @NotNull
    @Comment("요양보호사 차량소유여부")
    private boolean carOwnYn;

    @Column(name = "helper_edu_yn")
    @NotNull
    @Comment("요양보호사 치매 교육 이수 여부")
    private boolean eduYn;

    @Column(name = "helper_wage_state")
    @Comment("1:시급, 2:일급, 3:주급 구분")
    private Integer wageState;

    @Column(name = "helper_wage")
    @Comment("급여")
    private Integer wage;

    @Column(name = "helper_wage_negotiation")
    @Comment("급여 협의 가능 여부")
    private Boolean wageNegotiation;

    @Column(name = "helper_introduce", length = 255)
    @Comment("요양 보호사 자기 소개")
    private String introduce;

    @Column(name = "helper_exp")
    @Comment("간병경력")
    private Boolean is_experienced;

    @Column(name = "helper_time_negotiation")
    @Comment("요일 시간 가능 여부")
    private Boolean timeNegotiation;



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
    private int careGender; //남성:1 여성:2

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







    /* ==================================================
     * UPDATE
     * ================================================== */
    @Override
    public TblImg getImg() { return this.img; }

    @Override
    public void updateImg(TblImg img) { this.img = img; }
}
