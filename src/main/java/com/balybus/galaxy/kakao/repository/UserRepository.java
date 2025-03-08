package com.balybus.galaxy.kakao.repository;

import com.balybus.galaxy.kakao.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // JPA findBy 규칙
    // select * from user_master where kakao_email = ?
    public User findByKakaoEmail(String kakaoEmail);

    public User findByUserCode(String userCode);
}