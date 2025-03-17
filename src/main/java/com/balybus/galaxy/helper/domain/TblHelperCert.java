package com.balybus.galaxy.helper.domain;

import com.balybus.galaxy.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TblHelperCert extends BaseEntity {

    @Id
    @Column(name = "helper_cert_seq")
    @Comment("요양 보호사 자격증 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "helper_seq")
    private TblHelper tblHelper;

    @Column(name = "cert_name")
    @Comment("요양 보호자 자격증 이름")
    private String certName;

    @Column(name = "cert_num")
    @Comment("요양 보호사 자격증 번호")
    private String certNum;

    @Column(name = "cert_date_issue")
    @Comment("요양 보호사 자격증 발급일")
    private Integer certDateIssue;

    @Column(name = "cert_serial_num")
    @Comment("요양 보호사 자격증 내지 번호")
    private Integer certSerialNum;
}
