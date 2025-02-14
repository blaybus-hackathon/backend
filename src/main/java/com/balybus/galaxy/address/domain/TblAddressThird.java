package com.balybus.galaxy.address.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "at_name", nullable = false)
    private String name;
}
