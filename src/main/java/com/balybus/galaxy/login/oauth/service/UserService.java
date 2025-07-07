package com.balybus.galaxy.login.oauth.service;

import com.balybus.galaxy.global.config.aop.TimeTrace;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.login.oauth.client.KakaoApiToken;
import com.balybus.galaxy.login.oauth.client.KakaoApiUserInfo;
import com.balybus.galaxy.login.oauth.domain.type.CaseType;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.login.oauth.domain.TblKakao;
import com.balybus.galaxy.login.oauth.dto.request.KakaoRequest;
import com.balybus.galaxy.login.oauth.dto.request.KakaoUser;
import com.balybus.galaxy.login.oauth.dto.request.KakaoUserFeign;
import com.balybus.galaxy.login.oauth.dto.response.KakaoResponse;
import com.balybus.galaxy.login.oauth.dto.response.OauthToken;
import com.balybus.galaxy.login.oauth.repository.TblKakaoRepository;
import com.balybus.galaxy.login.classic.service.login.LoginServiceImpl;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserServiceImpl {

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
//        OauthToken oauthToken = getAccessToken(code.getCode());
//        log.info(oauthToken.getAccessToken());
//
//        KakaoUser userInfo = getUserInfo(oauthToken.getAccessToken());
//        log.info("카카오 사용자 정보: {}", userInfo);

        // FeignClient를 사용한 통신
        OauthToken oauthToken = kakaoApiClient.getToken(
                "authorization_code",
                clientId,
                redirectUri,
                code.getCode()
        );

        KakaoUserFeign userInfo = kakaoApiUserInfo.getUserInfo("Bearer " + oauthToken.getAccessToken());

        Optional<TblKakao> kakaoUser = tblKakaoRepository.findByKakaoEmail(userInfo.getEmail());
        Optional<TblUser> tblUser = memberRepository.findByEmail(userInfo.getEmail());

        ///
        boolean caseNum = false;
        if(kakaoUser.isEmpty() && tblUser.isPresent()) {
            // 케이스 1: 카카오 연동 정보는 없으나, 우리 서비스에 동일 이메일로 가입된 회원이 있는 경우
            // -> 기존 아이디로 자동 로그인 시키기 (카카오 계정과 기존 계정 자동 연동 및 로그인)
            String email = tblUser.get().getEmail();
            caseNum = true;
            loginService.signIn(MemberRequest.SignInDto.builder()
                    .userId(email)
                    .userPw(tblUser.get().getPassword())
                    .build(), request, response);
        }
        else if(kakaoUser.isEmpty() && tblUser.isEmpty()) {
            tblKakaoRepository.save(TblKakao.of(userInfo));
            // 케이스 2: 카카오 연동 정보도 없고, 우리 서비스 회원 정보도 없는 경우 (완전 신규)
            // -> 카카오 정보를 TblKakao에 저장하고, 프론트에 추가 회원가입 진행 안내
            return KakaoResponse.builder()
                    .email(userInfo.getEmail())
                    .nickName(userInfo.getNickname())
                    .loginType(LoginType.KAKAO_LOGIN)
                    .roleType(code.getRoleType())
                    .description("-카카오 회원 등록 완료 했습니다. 회원가입을 진행해 주세요.")
                    .caseType(CaseType.CASE2)
                    .build();
        }
        else if(kakaoUser.isPresent() && tblUser.isEmpty()) {
            // 케이스 3: 카카오 연동 정보는 있으나, 우리 서비스 회원 정보가 없는 경우
            // (예: 과거에 카카오로 우리 서비스에 접근 시도했으나, 회원가입을 완료하지 않은 경우 또는 다른 경로로 TblKakao만 생성된 경우)
            // -> 프론트에 추가 회원가입 진행 안내 (TblKakao 정보는 이미 있으므로 업데이트 또는 그대로 사용)
            return KakaoResponse.builder()
                    .email(userInfo.getEmail())
                    .nickName(userInfo.getNickname())
                    .loginType(LoginType.KAKAO_LOGIN)
                    .roleType(code.getRoleType())
                    .description("카카오 연동 정보는 있으나, 우리 서비스 회원 정보가 없습니다. 회원가입을 진행해 주세요.")
                    .caseType(CaseType.CASE3)
                    .build();
        }
        else if(kakaoUser.isPresent() && tblUser.isPresent()) {
            // 케이스 4: 카카오 연동 정보도 있고, 우리 서비스 회원 정보도 있는 경우 (이미 연동된 기존 회원)
            // -> 카카오 계정 기반으로 로그인 처리
            loginService.signIn(MemberRequest.SignInDto.builder()
                            .userId(kakaoUser.get().getKakaoEmail())
                            .userPw(tblUser.get().getPassword())
                    .build(), request, response);
        }

        if(caseNum) {
            return KakaoResponse.builder()
                    .email(userInfo.getEmail())
                    .nickName(userInfo.getNickname())
                    .loginType(LoginType.KAKAO_LOGIN)
                    .roleType(code.getRoleType())
                    .description("카카오 로그인이 정상적으로 완료되었습니다.")
                    .caseType(CaseType.CASE1)
                    .build();
        }
        return KakaoResponse.builder()
                .email(userInfo.getEmail())
                .nickName(userInfo.getNickname())
                .loginType(LoginType.KAKAO_LOGIN)
                .roleType(code.getRoleType())
                .description("카카오 로그인이 정상적으로 완료되었습니다.")
                .caseType(CaseType.CASE4)
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
