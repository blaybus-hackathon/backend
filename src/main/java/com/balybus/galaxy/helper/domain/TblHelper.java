package com.balybus.galaxy.helper.domain;

import com.balybus.galaxy.domain.BaseEntity;
import com.balybus.galaxy.helper.util.StringListConverter;
import com.balybus.galaxy.helper.util.WageConverter;
import com.balybus.galaxy.domain.tblImg.TblImg;
import com.balybus.galaxy.global.utils.file.ChangeProfileImg;
import com.balybus.galaxy.member.domain.TblUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.List;
import java.util.Map;

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

    @Column(name = "helper_address_detail", length = 255)
    @Comment("상세 주소")
    private String addressDetail;

    @Column(name = "helper_essential_cert_no", length = 11, nullable = false)
    @Comment("요양 보호사 자격증 번호")
    private String essentialCertNo;

    @Column(name = "helper_care_cert_no", length = 11, nullable = false)
    @Comment("간병사 자격증 번호")
    private String careCertNo;

    @Column(name = "helper_nurse_cert_no", length = 11, nullable = false)
    @Comment("병원 동행 매니저 자격증 번호")
    private String nurseCertNo;

    @Column(name = "helper_post_partum_cert_no", length = 11, nullable = false)
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

    @Column(name = "helper_wage")
    @Convert(converter = WageConverter.class)
    @Comment("희망급여(시급, 일급, 주급)")
    private Map<Integer, String> wage;

    @Column(name = "helper_wage_negotiation")
    @Comment("급여 협의 가능 여부")
    private Boolean wageNegotiation;

    @Column(name = "helper_introduce")
    @Comment("요양 보호사 자기 소개")
    private String introduce;

    @Column(name = "helper_strength")
    @Comment("요양 보호사 강점")
    @Convert(converter = StringListConverter.class)
    private List<String> strengths;


    /* ==================================================
     * UPDATE
     * ================================================== */
    @Override
    public TblImg getImg() { return this.img; }

    @Override
    public void updateImg(TblImg img) { this.img = img; }
}
