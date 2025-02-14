package com.balybus.galaxy.global.config.jwt;

import com.balybus.galaxy.login.domain.type.RoleType;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.member.dto.response.MemberResponse;
import com.balybus.galaxy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1. 이메일로 조회
        Optional<TblUser> userOpt = memberRepository.findByEmail(username);
        if(userOpt.isEmpty()) throw new UsernameNotFoundException("User not found");

        //2. 데이터 반환
        TblUser user = userOpt.get();
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(List.of(() -> user.getUserAuth().getCode()))
                .build();
    }
}
