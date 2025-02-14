package com.balybus.galaxy.helper.domain;

import com.balybus.galaxy.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TblHelperWorkLocation extends BaseEntity {

    @Id
    @Column(name = "hwl_seq")
    @Comment(value="근무 가능 지역 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("요양보호사 정보 구분자")
    @ManyToOne
    @JoinColumn(name = "helper_seq")
    @NotNull
    private TblHelper helper;

    @Column(name = "af_addr")
    @Comment("시.도 구분자")
    private String tblAddressFirst;

    @Column(name = "as_addr")
    @Comment("시.군.구 구분자")
    private String tblAddressSecond;

    @Column(name = "at_addr")
    @Comment("읍.면.동 구분자")
    private String tblAddressThird;
}
