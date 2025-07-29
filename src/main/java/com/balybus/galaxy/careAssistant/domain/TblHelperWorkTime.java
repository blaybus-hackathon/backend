package com.balybus.galaxy.careAssistant.domain;

import com.balybus.galaxy.global.domain.BaseEntity;
import com.balybus.galaxy.careAssistant.util.StringListConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TblHelperWorkTime extends BaseEntity {

    @Id
    @Column(name = "hwt_seq")
    @Comment("근무 가능 시간 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Comment("요양보호사 정보 구분자")
    @JoinColumn(name = "helper_seq")
    private TblHelper helper;

    @Column(name = "hwt_date")
    @Comment("근무 가능 요일")
    @NotNull
    private Integer date;

    @Column(name = "hwt_start_time")
    @Comment("시작시간")
    @NotNull
    private String startTime;

    @Column(name = "hwt_end_time")
    @Comment("종료시간")
    @NotNull
    private String endTime;

    @Column(name = "hwt_nego")
    @Comment("근무 협의 여부")
    private Boolean negotiation;

    @Column(name = "hwt_work_term")
    @Comment("근무 가능 기간")
    private String workTerm;
}
