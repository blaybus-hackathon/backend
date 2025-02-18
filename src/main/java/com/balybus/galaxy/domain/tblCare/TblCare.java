package com.balybus.galaxy.domain.tblCare;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TblCare {
    @Id
    @Column(name="care_seq")
    @Comment(value="어르신 케어항목 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            //어르신 케어항목 구분자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "top_care_seq")
    @Comment("상위 구분자(null 인 경우 root)")
    private TblCare care;       //상위 구분자

    @Column(nullable = false)
    @Comment("어르신 케어항목")
    private String careName;    //어르신 케어항목

    @Comment("비트 전환시 해당되는 값(1/2/4/8/16... 으로 할당됨)")
    private Integer careVal;    //비트 전환시 해당되는 값(1/2/4/8/16... 으로 할당됨)

    @Column(nullable = false)
    @Comment("사용여부")
    private boolean careYn;     //사용여부
}
