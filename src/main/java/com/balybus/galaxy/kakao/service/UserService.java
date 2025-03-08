package com.balybus.galaxy.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.balybus.galaxy.kakao.model.User;
import com.balybus.galaxy.kakao.model.oauth.KakaoProfile;
import com.balybus.galaxy.kakao.model.oauth.OauthToken;
import com.balybus.galaxy.kakao.repository.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public OauthToken getAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "6d14dc79fabe2059d567d923273f3225");
        params.add("redirect_uri", "https://cozyinfo.vercel.app/main");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return oauthToken;
    }

    public User saveUser(String token) throws InterruptedException {
        KakaoProfile profile = findProfile(token);

        if (profile == null || profile.getKakaoAccount() == null || profile.getKakaoAccount().getEmail() == null) {
            throw new IllegalArgumentException("Kakao profile or email is missing");
        }

        KakaoProfile.KakaoAccount kakaoAccount = profile.getKakaoAccount();
        User user = userRepository.findByKakaoEmail(kakaoAccount.getEmail());

        if (user == null) {
            // 카카오 프로필 정보 확인 및 null 처리
            String profileImgUrl = kakaoAccount.getProfile() != null ? kakaoAccount.getProfile().getProfileImageUrl() : null;
            String nickname = kakaoAccount.getProfile() != null ? kakaoAccount.getProfile().getNickname() : null;

            user = User.builder()
                    .kakaoId(profile.getId())
                    .kakaoProfileImg(profileImgUrl)  // null 체크 후 값 세팅
                    .kakaoNickname(nickname)  // null 체크 후 값 세팅
                    .kakaoEmail(kakaoAccount.getEmail())
                    .userRole("ROLE_USER")
                    .createTime(new Timestamp(System.currentTimeMillis())) // createTime 값 세팅
                    .build();

            userRepository.save(user);
        }

        return user;
    }



    public KakaoProfile findProfile(String token) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<Void> kakaoProfileRequest = new HttpEntity<>(headers);  // ✅ 수정

        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }
}
