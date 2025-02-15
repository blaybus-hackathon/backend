package com.balybus.galaxy.address.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TblAddressSecond {

    @Id
    @Column(name = "as_seq")
    @Comment("시.군.구 구분자")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "af_seq", nullable = false)
    @Comment("시.도 구분자")
    private TblAddressFirst addressFirst;

    @Column(name = "as_name", nullable = false, length = 50)
    @Comment("시.군.구 명")
    private String name;
}
