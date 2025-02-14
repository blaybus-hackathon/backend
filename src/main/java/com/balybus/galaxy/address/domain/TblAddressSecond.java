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


    @Column(name = "as_name")
    private String name;
}
