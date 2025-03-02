package com.balybus.galaxy.kakaoApi.serviceImpl.service;

import com.balybus.galaxy.kakaoApi.DTO.KakaoDTO;
import com.balybus.galaxy.kakaoApi.repository.UserRepository;
import com.balybus.galaxy.kakaoApi.KakaoUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoUtil kakaoUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User oAuthLogin(String accessCode, HttpServletResponse response) {
        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);

        return (User) User.withUsername(kakaoProfile.getKakao_account().getEmail())
                .password(passwordEncoder.encode("defaultPassword"))
                .roles("USER")
                .build();
    }
}
