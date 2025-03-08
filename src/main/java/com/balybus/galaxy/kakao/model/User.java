package com.balybus.galaxy.kakao.model;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_code")
    private Long userCode;

    @Column(name = "kakao_id")
    private Long kakaoId;

    @Column(name = "kakao_profile_img")
    private String kakaoProfileImg;

    @Column(name = "kakao_nickname")
    private String kakaoNickname;

    @Column(name = "kakao_email")
    private String kakaoEmail;

    @Column(name = "user_role")
    private String userRole;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createTime;

    // Lombok @Builder가 추가되어 있으므로, 빌더 메서드를 사용할 수 있습니다.
}
