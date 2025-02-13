package com.balybus.galaxy.helper.domain;

import com.balybus.galaxy.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EntityListeners(value = AuditingEntityListener.class)
public class Helper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false)
    private Member member;

    @Column(length = 10)
    private String name;

    @Column(length = 11)
    private String phone;

    @Column(length = 255)
    private String addressDetail;

    @Column(length = 11, nullable = false)
    private String essentialCertNo;

    @Column(nullable = false)
    private boolean carOwnYn;

    @Column(nullable = false)
    private boolean eduYn;

    @Column(nullable = false)
    private int wage;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
