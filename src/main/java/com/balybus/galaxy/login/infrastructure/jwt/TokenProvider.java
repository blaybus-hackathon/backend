package com.balybus.galaxy.login.infrastructure.jwt;

import com.balybus.galaxy.global.config.jwt.ExpiredTokenException;
import com.balybus.galaxy.global.config.jwt.InvalidTokenException;
import com.balybus.galaxy.global.exception.BadRequestException;
import io.jsonwebtoken.*;
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
        } catch (ExpiredJwtException e) { // 🔹 만료된 토큰
            throw new ExpiredTokenException("만료된 토큰입니다. 다시 로그인해주세요.");
        } catch (UnsupportedJwtException e) { // 지원되지 않는 토큰
            throw new InvalidTokenException("지원되지 않는 JWT 토큰입니다.");
        } catch (MalformedJwtException e) { // 토큰이 잘못된 형식일 때
            throw new InvalidTokenException("잘못된 형식의 JWT 토큰입니다.");
        } catch (JwtException e) { // 그외 JWT 관련 예외
            throw new InvalidTokenException("유효하지 않은 JWT 토큰 형식입니다.");
        } catch (IllegalArgumentException e) { // 🔹 토큰이 비어있거나 잘못된 경우
            throw new InvalidTokenException("JWT 토큰이 비어있거나 잘못되었습니다.");
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
