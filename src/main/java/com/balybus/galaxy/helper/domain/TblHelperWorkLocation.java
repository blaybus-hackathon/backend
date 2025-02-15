package com.balybus.galaxy.helper.domain;

import com.balybus.galaxy.address.domain.TblAddressFirst;
import com.balybus.galaxy.address.domain.TblAddressSecond;
import com.balybus.galaxy.address.domain.TblAddressThird;
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

    @Comment("시.도 구분자")
    @ManyToOne
    @NotNull
    @JoinColumn(name = "af_seq", nullable = false)
    private TblAddressFirst tblAddressFirst;

    @Comment("시.군.구 구분자")
    @ManyToOne
    @JoinColumn(name = "as_seq", nullable = false)
    @NotNull
    private TblAddressSecond tblAddressSecond;

    @Comment("읍.면.동 구분자")
    @ManyToOne
    @JoinColumn(name = "at_seq", nullable = false)
    @NotNull
    private TblAddressThird tblAddressThird;
}
