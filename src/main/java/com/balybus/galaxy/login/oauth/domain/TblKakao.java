package com.balybus.galaxy.login.oauth.domain;

import com.balybus.galaxy.global.domain.BaseEntity;
import com.balybus.galaxy.login.oauth.dto.request.KakaoUserFeign;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EntityListeners(value = AuditingEntityListener.class)
public class TblKakao extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_id")
    @Comment("카카오 아이디")
    private String kakaoId;

    @Column(name = "kakao_email")
    @Comment("카카오 이메일")
    private String kakaoEmail;

    @Column(name = "kakao_nickname")
    @Comment("카카오 닉네임")
    private String kakaoNickname;

    @Column(name = "kakao_profileImage")
    private String kakaoProfileImage;

    public static TblKakao of(KakaoUserFeign kakaoUser) {
        return TblKakao.builder()
                .kakaoId(kakaoUser.getId())
                .kakaoEmail(kakaoUser.getEmail())
                .kakaoNickname(kakaoUser.getNickname())
                .kakaoProfileImage(kakaoUser.getProfileImage())
                .build();
    }
}
