package com.balybus.galaxy.address.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Immutable;

import java.util.List;

@Entity
@AllArgsConstructor
@Immutable
@NoArgsConstructor
@Builder
@Getter
public class TblAddressFirst {

    @Id
    @Column(name = "af_seq")
    @Comment("시.도 구분자")
    private Long id;

    @Column(name = "af_name", nullable = false, length = 50)
    @Comment("시.군.구 명")
    private String name;

    @OneToMany(mappedBy = "addressFirst", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<TblAddressSecond> addressSeconds;
}
