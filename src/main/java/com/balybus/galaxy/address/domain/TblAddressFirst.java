package com.balybus.galaxy.address.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TblAddressFirst {

    @Id
    @Column(name = "af_seq")
    @Comment("시.도 구분자")
    private Long id;

    @Column(name = "af_name")
    @Comment("시.군.구 명")
    private String name;
}
