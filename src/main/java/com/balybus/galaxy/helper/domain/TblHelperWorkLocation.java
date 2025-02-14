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
}
