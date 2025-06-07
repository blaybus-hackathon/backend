package com.balybus.galaxy.login.oauth.repository;

import com.balybus.galaxy.login.oauth.domain.TblKakao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TblKakaoRepository extends JpaRepository<TblKakao, Long> {

    Optional<TblKakao> findByKakaoEmail(String kakaoEmail);
}
