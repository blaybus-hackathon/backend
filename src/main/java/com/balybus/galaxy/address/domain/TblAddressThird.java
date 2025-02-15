package com.balybus.galaxy.address.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TblAddressThird {

    @Id
    @Column(name = "at_seq")
    private Long id;

    @JoinColumn(name = "as_seq", nullable = false)
    @Comment("시.군.구 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblAddressSecond addressSecond;

    @Column(name = "at_name", nullable = false, length = 50)
    @Comment("읍.면.동 명")
    private String name;
}
