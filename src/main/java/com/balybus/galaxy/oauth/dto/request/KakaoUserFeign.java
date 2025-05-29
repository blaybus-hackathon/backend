package com.balybus.galaxy.oauth.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserFeign {

    private String id;

    @JsonProperty("properties")
    private KakaoProperties properties;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public String getNickname() {
        if (this.properties != null && this.properties.getNickname() != null) {
            return this.properties.getNickname();
        }
        if (this.kakaoAccount != null && this.kakaoAccount.getProfile() != null && this.kakaoAccount.getProfile().getNickname() != null) {
            return this.kakaoAccount.getProfile().getNickname();
        }
        return null;
    }

    public String getProfileImage() {
        if (this.properties != null && this.properties.getProfileImage() != null) {
            return this.properties.getProfileImage();
        }
        if (this.kakaoAccount != null && this.kakaoAccount.getProfile() != null && this.kakaoAccount.getProfile().getProfileImageUrl() != null) {
            return this.kakaoAccount.getProfile().getProfileImageUrl();
        }
        return null;
    }

    public String getEmail() {
        if (this.kakaoAccount != null) {
            return this.kakaoAccount.getEmail();
        }
        return null;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoProperties {
        private String nickname;

        @JsonProperty("profile_image")
        private String profileImage;

        @JsonProperty("thumbnail_image")
        private String thumbnailImage;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {
        private String email;

        @JsonProperty("profile_nickname_needs_agreement")
        private Boolean profileNicknameNeedsAgreement;

        @JsonProperty("profile_image_needs_agreement")
        private Boolean profileImageNeedsAgreement;

        private KakaoProfile profile;

        @JsonProperty("email_needs_agreement")
        private Boolean emailNeedsAgreement;

        @JsonProperty("is_email_valid")
        private Boolean isEmailValid;

        @JsonProperty("is_email_verified")
        private Boolean isEmailVerified;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoProfile {
        private String nickname;

        @JsonProperty("thumbnail_image_url")
        private String thumbnailImageUrl;

        @JsonProperty("profile_image_url")
        private String profileImageUrl;

        @JsonProperty("is_default_image")
        private Boolean isDefaultImage;
    }
}