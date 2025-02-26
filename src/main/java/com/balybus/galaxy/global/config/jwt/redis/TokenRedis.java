package com.balybus.galaxy.global.config.jwt.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value = "token", timeToLive = 2592000) // 리프레시토큰의 expiretime(ms) 을 초로 전환
public class TokenRedis {
    @Id
    private String id;

    @Indexed
    private String accessToken;

    private String refreshToken;

    /* ===========================================================
     * UPDATE
     * =========================================================== */
    public void updateAccessToken(String accessToken){
        this.accessToken = accessToken;
    }
}
