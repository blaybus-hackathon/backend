package com.balybus.galaxy.kakao.service;

import com.balybus.galaxy.kakao.dto.request.KakaoRequest;
import com.balybus.galaxy.kakao.dto.request.KakaoUser;
import com.balybus.galaxy.kakao.dto.response.OauthToken;
import com.balybus.galaxy.login.domain.type.RoleType;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {


    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.token-uri}")
    private String tokenUri;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public KakaoUser kakaoLogin(KakaoRequest code) {
        // 인가 코드를 통해 access_token 발급
        OauthToken oauthToken = getAccessToken(code.getCode());
        log.info(oauthToken.getAccessToken());

        KakaoUser userInfo = getUserInfo(oauthToken.getAccessToken());
        log.info("카카오 사용자 정보: {}", userInfo);

        // 로그인 진행
        doKakaoLogin(oauthToken, userInfo, code);

        return userInfo;
    }

    private void doKakaoLogin(OauthToken oauthToken, KakaoUser userInfo, KakaoRequest code) {
        // 요양 보호사 & 센터 회원 가입 진행
        String userPassword = clientSecret + oauthToken.getAccessToken();
        TblUser user = TblUser.builder()
                .email(userInfo.getEmail())
                .password(bCryptPasswordEncoder.encode(userPassword))
                .userAuth(code.getRoleType())
                .build();

        memberRepository.save(user);
    }

    private KakaoUser getUserInfo(String accessToken) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                userInfoUri,
                HttpMethod.GET,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        KakaoUser kakaoUser = new KakaoUser();
        try {
            jsonNode = objectMapper.readTree(kakaoProfileResponse.getBody());

            kakaoUser.setId(jsonNode.get("id").asText());
            kakaoUser.setEmail(jsonNode.path("kakao_account").path("email").asText());
            kakaoUser.setNickname(jsonNode.path("properties").path("nickname").asText());
            kakaoUser.setProfileImage(jsonNode.path("properties").path("profile_image").asText());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoUser;
    }
    private OauthToken getAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = rt.exchange(
                tokenUri,
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
}
