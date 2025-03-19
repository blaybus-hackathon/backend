package com.balybus.galaxy.kakao.domain;

import com.balybus.galaxy.domain.BaseEntity;
import com.balybus.galaxy.kakao.dto.request.KakaoUser;
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

    public static TblKakao of(KakaoUser kakaoUser) {
        return TblKakao.builder()
                .kakaoId(kakaoUser.getId())
                .kakaoEmail(kakaoUser.getEmail())
                .kakaoNickname(kakaoUser.getNickname())
                .kakaoProfileImage(kakaoUser.getProfileImage())
                .build();
    }
}
