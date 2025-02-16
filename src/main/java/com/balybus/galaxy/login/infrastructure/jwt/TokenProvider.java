package com.balybus.galaxy.login.infrastructure.jwt;

import com.balybus.galaxy.global.exception.BadRequestException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

import static com.balybus.galaxy.global.exception.ExceptionCode.INVALID_REFRESH_TOKEN;

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
        return createToken(subject, accessExpirationTime);
    }

    public String refreshToken(String subject) {
        return createToken(subject, refreshExpirationTime);
    }

    public String renewAccessToken(String refreshToken) {
        if(!validateToken(refreshToken)) {
            throw new BadRequestException(INVALID_REFRESH_TOKEN);
        }
        String username = getUsername(refreshToken);
        return generateAccessToken(username);
    }

    /**
     * 엑세스 토큰 발급 / 갱신
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

    // 토큰에서 사용자명 추출
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 요청에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
