package com.balybus.galaxy.login.infrastructure.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenProvider {


    private static final String EMPTY_STRING = "";
    private final String issuer;
    private final Key secretKey;
    private final Long accessExpirationTime;
    private final Long refreshExpirationTime;

    public TokenProvider(
            @Value("${jwt.issuer}") final String issuer,
            @Value("${jwt.secret-key}") final String secretKey,
            @Value("${jwt.access-expiration-time}") final Long accessExpirationTime,
            @Value("${jwt.refresh-expiration-time}") final Long refreshExpirationTime
    ) {
        this.issuer = issuer;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    /**
     * 엑세스 토큰 발급 / 갱신
     * @param subject
     * @return
     */
    public String generateAccessToken(String subject) {
        final String accessToken = createToken(subject, accessExpirationTime);
        return accessToken;
    }

    public String refreshToken() {
        return createToken(EMPTY_STRING, refreshExpirationTime);
    }

    /**
     * 토큰 발급 / 갱신
     */
    private String createToken(String subject, final Long tokenExpirationTime) {
        Date now = new Date();
        final Date expiry = new Date(now.getTime() + tokenExpirationTime);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(subject)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
