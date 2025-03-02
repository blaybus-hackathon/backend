package com.balybus.galaxy.kakaoApi.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")  // 실제 DB 테이블명
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String nickname;
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;
}
