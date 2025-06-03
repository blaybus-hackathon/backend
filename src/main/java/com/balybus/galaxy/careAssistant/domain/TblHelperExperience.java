package com.balybus.galaxy.careAssistant.domain;

import com.balybus.galaxy.global.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TblHelperExperience extends BaseEntity {

    @Id
    @Column(name = "he_seq")
    @Comment("경력 사항 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "helper_seq")
    @Comment("요양 보호사 정보 구분자")
    private TblHelper helper;

    @Column(name = "filed")
    @Comment("근무지")
    private String field;

    @Column(name = "he_start_date")
    @Comment("근무시작일")
    @NotNull
    private LocalDateTime heStartDate;

    @Column(name = "he_end_date")
    @Comment("근무종료일")
    private LocalDateTime heEndDate;
}
