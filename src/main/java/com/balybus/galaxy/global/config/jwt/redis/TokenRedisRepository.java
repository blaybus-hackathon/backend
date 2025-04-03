package com.balybus.galaxy.global.config.jwt.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRedisRepository extends CrudRepository<TokenRedis, String> {
    Optional<TokenRedis> findByAccessToken(String accessToken);
}
