package com.balybus.galaxy.oauth.service;

import com.balybus.galaxy.global.config.aop.TimeTrace;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.oauth.client.KakaoApiToken;
import com.balybus.galaxy.oauth.client.KakaoApiUserInfo;
import com.balybus.galaxy.oauth.domain.TblKakao;
import com.balybus.galaxy.oauth.dto.request.KakaoRequest;
import com.balybus.galaxy.oauth.dto.request.KakaoUser;
import com.balybus.galaxy.oauth.dto.request.KakaoUserFeign;
import com.balybus.galaxy.oauth.dto.response.KakaoResponse;
import com.balybus.galaxy.oauth.dto.response.OauthToken;
import com.balybus.galaxy.oauth.repository.TblKakaoRepository;
import com.balybus.galaxy.login.serviceImpl.login.LoginServiceImpl;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.member.domain.type.LoginType;
import com.balybus.galaxy.member.dto.request.MemberRequest;
import com.balybus.galaxy.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.token-uri}")
    private String tokenUri;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    private final MemberRepository memberRepository;
    private final TblKakaoRepository tblKakaoRepository;
    private final LoginServiceImpl loginService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KakaoApiToken kakaoApiClient;
    private final KakaoApiUserInfo kakaoApiUserInfo;

    private final WebClient webClient = WebClient.builder().build();

    @TimeTrace
    public KakaoResponse kakaoLogin(KakaoRequest code, HttpServletRequest request, HttpServletResponse response) {
        // 인가 코드를 통해 access_token 발급
        OauthToken oauthToken = getAccessToken(code.getCode());
        log.info(oauthToken.getAccessToken());

        KakaoUser userInfo = getUserInfo(oauthToken.getAccessToken());
        log.info("카카오 사용자 정보: {}", userInfo);

        // FeignClient를 사용한 통신
//        OauthToken oauthToken = kakaoApiClient.getToken(
//                "authorization_code",
//                clientId,
//                redirectUri,
//                code.getCode()
//        );
//
//        KakaoUserFeign userInfo = kakaoApiUserInfo.getUserInfo("Bearer " + oauthToken.getAccessToken());
//
        Optional<TblKakao> kakaoUser = tblKakaoRepository.findByKakaoEmail(userInfo.getEmail());
        Optional<TblUser> tblUser = memberRepository.findByEmail(userInfo.getEmail());

        ///

        if(kakaoUser.isEmpty() && tblUser.isPresent()) {
            // 기존 아이디로 자동 로그인 시키기
            String email = tblUser.get().getEmail();
            loginService.signIn(MemberRequest.SignInDto.builder()
                    .userId(email)
                    .userPw(tblUser.get().getPassword())
                    .build(), request, response);
        }
        else if(kakaoUser.isEmpty() && tblUser.isEmpty()) {
            tblKakaoRepository.save(TblKakao.of(userInfo));
            //이메일과 닉네임을 전달하고 kakao로그인 구분자를 전달해주어 프론트 측에서 이후 회원가입 진행시 해당 정보를 다시 넘겨줄수 있도록 하면 좋을거 같습니다.
            return KakaoResponse.builder()
                    .email(userInfo.getEmail())
                    .nickName(userInfo.getNickname())
                    .loginType(LoginType.KAKAO_LOGIN)
                    .description("카카오 회원 등록 완료. 회원가입을 진행해 주세요.")
                    .build();
        }
        else if(kakaoUser.isPresent() && tblUser.isEmpty()) {
            //이메일과 닉네임을 전달하고 kakao로그인 구분자를 전달해주어 프론트 측에서 이후 회원가입 진행시 해당 정보를 다시 넘겨줄수 있도록 하면 좋을거 같습니다.
            return KakaoResponse.builder()
                    .email(userInfo.getEmail())
                    .nickName(userInfo.getNickname())
                    .loginType(LoginType.KAKAO_LOGIN)
                    .description("회원가입을 진행해 주세요.")
                    .build();
        }
        else if(kakaoUser.isPresent() && tblUser.isPresent()) {
            // TblKakao와 TblUser 모두 있다면, 로그인 시도이므로 기존 일반 로그인의 로직(LoginServiceImpl 클래스내 signIn 매소드)을 사용하여 데이터를 넘겨주면 됩니다.
            loginService.signIn(MemberRequest.SignInDto.builder()
                            .userId(kakaoUser.get().getKakaoEmail())
                            .userPw(tblUser.get().getPassword())
                    .build(), request, response);
        }

        return KakaoResponse.builder()
                .email(userInfo.getEmail())
                .nickName(userInfo.getNickname())
                .loginType(LoginType.KAKAO_LOGIN)
                .description("로그인이 정상적으로 완료되었습니다.")
                .build();
    }

    /**
     * 비동기 WebClient 방식
     * @param accessToken
     * @return
     */
    private KakaoUser getUserInfo(String accessToken) {
        String response = webClient.get()
                .uri(userInfoUri)
                .headers(headers -> {
                    headers.add("Authorization", "Bearer " + accessToken);
                    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
                })
                .retrieve()
                .bodyToMono(String.class)
                .block();

        KakaoUser kakaoUser = new KakaoUser();
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            kakaoUser.setId(jsonNode.get("id").asText());
            kakaoUser.setEmail(jsonNode.path("kakao_account").path("email").asText());
            kakaoUser.setNickname(jsonNode.path("properties").path("nickname").asText());
            kakaoUser.setProfileImage(jsonNode.path("properties").path("profile_image").asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return kakaoUser;
    }

    /**
     * 비동기 WebClient 방식
     * @param code
     * @return
     */
    private OauthToken getAccessToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        String response = webClient.post()
                .uri(tokenUri)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .bodyValue(params)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        OauthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response, OauthToken.class);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(ExceptionCode.TOKEN_INFO_INVALID);
        }

        return oauthToken;
    }

    /**
     * 동기 방식
     * @param accessToken
     * @return
     */
    private KakaoUser getUserInfoRT(String accessToken) {
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


    /**
     * RestTemplate 동시 방식
     * @param code
     * @return
     */
    private OauthToken getAccessTokenRT(String code) {
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
