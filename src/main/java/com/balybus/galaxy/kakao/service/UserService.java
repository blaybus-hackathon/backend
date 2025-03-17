package com.balybus.galaxy.kakao.service;

import com.balybus.galaxy.kakao.dto.response.OauthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class UserService {

    public String kakaoLogin(String code) throws InterruptedException {
        // 인가 코드를 통해 access_token 발급
        OauthToken oauthToken = getAccessToken(code);
        log.info(oauthToken.getAccessToken());
        // 발급 받은 accessToken으로 카카오 회원 정보 DB 저장
//        User user = saveUser(oauthToken.getAccessToken());

        return oauthToken.getAccessToken();
    }

    private OauthToken getAccessToken(String code) {
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

        log.info(" [Kakao Service] Access Token ------> {}", oauthToken.getAccessToken());
        log.info(" [Kakao Service] Refresh Token ------> {}", oauthToken.getRefreshToken());
        //제공 조건: OpenID Connect가 활성화 된 앱의 토큰 발급 요청인 경우 또는 scope에 openid를 포함한 추가 항목 동의 받기 요청을 거친 토큰 발급 요청인 경우
        log.info(" [Kakao Service] Id Token ------> {}", oauthToken.getIdToken());
        log.info(" [Kakao Service] Scope ------> {}", oauthToken.getScope());

        return oauthToken;
    }

//    private User saveUser(String token) throws InterruptedException {
//        KakaoProfile profile = findProfile(token);
//
//        if (profile == null || profile.getKakaoAccount() == null || profile.getKakaoAccount().getEmail() == null) {
//            throw new IllegalArgumentException("Kakao profile or email is missing");
//        }
//
//        KakaoProfile.KakaoAccount kakaoAccount = profile.getKakaoAccount();
//        User user = userRepository.findByKakaoEmail(kakaoAccount.getEmail());
//
//        if (user == null) {
//            // 카카오 프로필 정보 확인 및 null 처리
//            String profileImgUrl = kakaoAccount.getProfile() != null ? kakaoAccount.getProfile().getProfileImageUrl() : null;
//            String nickname = kakaoAccount.getProfile() != null ? kakaoAccount.getProfile().getNickname() : null;
//
//            user = User.builder()
//                    .kakaoId(profile.getId())
//                    .kakaoProfileImg(profileImgUrl)  // null 체크 후 값 세팅
//                    .kakaoNickname(nickname)  // null 체크 후 값 세팅
//                    .kakaoEmail(kakaoAccount.getEmail())
//                    .userRole("ROLE_USER")
//                    .createTime(new Timestamp(System.currentTimeMillis())) // createTime 값 세팅
//                    .build();
//
//            userRepository.save(user);
//        }
//
//        return user;
//    }
//
//
//
//    public KakaoProfile findProfile(String token) {
//        RestTemplate rt = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + token);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        HttpEntity<Void> kakaoProfileRequest = new HttpEntity<>(headers);  // ✅ 수정
//
//        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                kakaoProfileRequest,
//                String.class
//        );
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        KakaoProfile kakaoProfile = null;
//        try {
//            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        return kakaoProfile;
//    }
}
