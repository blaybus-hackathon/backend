package com.balybus.galaxy.member.domain;

import com.balybus.galaxy.domain.BaseEntity;
import com.balybus.galaxy.login.domain.type.RoleType;
import com.balybus.galaxy.member.domain.type.LoginType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EntityListeners(value = AuditingEntityListener.class)
public class TblUser extends BaseEntity {

    @Id
    @Column(name = "user_seq")
    @Comment("유저 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 30, nullable = false, unique = true)
    @Comment("로그인 아이디")
    private String email; // USER_ID

    @Column(name = "user_pe", length = 255, nullable = false)
    @Comment("로그인 비밀번호")
    private String password;

    @Comment("JWT 리프레시 토큰")
    @Column(name = "user_refresh_token", nullable = true)
    private String refreshToken;

    @Column(name = "user_auth")
    @Enumerated(EnumType.STRING)
    @Comment("권한 구분")
    private RoleType userAuth;

    @Column(name = "user_login_type")
    @Comment("로그인 타입 구분자")
    @Enumerated(EnumType.STRING)
    private LoginType userLoginType;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public void updatePwd(String pwd) {
        this.password = new BCryptPasswordEncoder().encode(pwd);;
    }
}
