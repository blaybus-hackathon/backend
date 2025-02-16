package com.balybus.galaxy.helper.domain;

import com.balybus.galaxy.domain.BaseEntity;
import com.balybus.galaxy.helper.util.StringListConverter;
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
public class TblHelper extends BaseEntity {

    @Id
    @Column(name = "helper_seq")
    @Comment("요양보호사 정보 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @Comment("유저 구분자")
    @JoinColumn(name = "user_seq", nullable = false)
    private TblUser user;

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
    @Comment("희망급여(시급)")
    private Integer wage;
}
