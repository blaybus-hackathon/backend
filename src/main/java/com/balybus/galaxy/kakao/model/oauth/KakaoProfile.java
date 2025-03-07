package com.balybus.galaxy.kakao.model.oauth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoProfile {

    private Long id;
    private String connected_at;
    private KakaoAccount kakao_account;

    public long getId() {
        return id; // 실제 id 반환
    }

    public KakaoAccount getKakaoAccount() {
        return kakao_account; // 실제 KakaoAccount 반환
    }

    @Getter
    @Setter
    public static class KakaoAccount {
        private Boolean profileNicknameNeedsAgreement;
        private Boolean profileImageNeedsAgreement;
        private Profile profile;
        private Boolean hasEmail;
        private Boolean emailNeedsAgreement;
        private Boolean isEmailValid;
        private Boolean isEmailVerified;
        private String email;

        public String getEmail() {
            return email; // 실제 email 반환
        }

        public Profile getProfile() {
            return profile; // 실제 Profile 반환
        }

        @Getter
        @Setter
        public static class Profile {
            private String nickname;
            private String thumbnailImageUrl;
            private String profileImageUrl;
            private Boolean isDefaultImage;

            // 프로필 이미지 URL 반환
            public String getProfileImageUrl() {
                return profileImageUrl; // 실제 값 반환
            }

            // 닉네임 반환
            public String getNickname() {
                return nickname; // 실제 값 반환
            }
        }
    }
}
