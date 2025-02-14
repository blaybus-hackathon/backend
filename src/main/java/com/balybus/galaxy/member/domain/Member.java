package com.balybus.galaxy.member.domain;

import com.balybus.galaxy.login.domain.type.RoleType;
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
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false, unique = true)
    private String email; // USER_ID


    @Column(length = 255, nullable = false)
    private String password;

    @Column(name = "refresh_token", nullable = true)
    private String refreshToken;

    @Column(name = "user_auth")
    @Enumerated(EnumType.STRING)
    private RoleType userAuth;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
