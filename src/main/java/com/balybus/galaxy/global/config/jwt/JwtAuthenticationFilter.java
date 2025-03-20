package com.balybus.galaxy.global.config.jwt;

import com.balybus.galaxy.login.infrastructure.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final CookieUtils cookieUtils;
    private final UserDetailsService userDetailsService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthenticationFilter(TokenProvider tokenProvider, CookieUtils cookieUtils, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.cookieUtils = cookieUtils;
        this.userDetailsService = userDetailsService;
    }

    // permitAll uri 패턴 검사
    private boolean checkUri(String requestUri) {
        List<String> uriPatterns = List.of(
                "/",
                "/api/sign/**", "/api/token/**", "/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**",
                "/img/**", "/css/**", "/static/js/**", "/docs/**",
                "/ws-chat", "/page/**"
        );

        for (String pattern : uriPatterns) {
            if (pathMatcher.match(pattern, requestUri)) return true;
        }

        return false;
    }

    // 쿠키 사용
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 요청 uri 검사
        String requestUri = request.getRequestURI();
        if(!checkUri(requestUri)) {
            // 2. 토큰 추출
            Optional<Cookie> accessToken = cookieUtils.getCookie(request,"JWT-TOKEN" );
            if(accessToken.isEmpty()) {
                // 토큰이 없음.
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "액세스 토큰이 없습니다.");
                return;
            }
            String token = String.valueOf(accessToken.get().getValue());

            // 3. 토큰 유효성 검사
            String username;
            try{
                boolean tokenValidity = tokenProvider.validateToken(token);
                if(!tokenValidity) {
                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 JWT 토큰 형식입니다.");
                    return;
                }
                username = tokenProvider.getUsername(token);
            } catch (ExpiredTokenException e){
                // 만료된 토큰 예외
                username = tokenProvider.replaceAccessToken(request, response, token);
            } catch (InvalidTokenException e) {
                // 유효하지 않은 토큰 예외
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
            } catch (Exception e) {
                // 그 외 발생 오류
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류가 발생하였습니다.");
                return;
            }

            // 4. 토큰이 유효하면 SecurityContext에 저장
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }



//    // 쿠키 미사용
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        // 1. 요청 uri 검사
//        String requestUri = request.getRequestURI();
//        if(!checkUri(requestUri)) {
//            // 2. 토큰 추출
//            String token = tokenProvider.resolveToken(request);
//            if(token == null) {
//                // 토큰이 없음.
//                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "액세스 토큰이 없습니다.");
//                return;
//            }
//
//            // 3. 토큰 유효성 검사
//            try{
//                boolean tokenValidity = tokenProvider.validateToken(token);
//                if(!tokenValidity) {
//                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 JWT 토큰 형식입니다.");
//                    return;
//                }
//            } catch (ExpiredTokenException | InvalidTokenException e) {
//                // 만료된 토큰 예외 | 유효하지 않은 토큰 예외
//                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
//                return;
//            } catch (Exception e) {
//                // 그 외 발생 오류
//                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류가 발생하였습니다.");
//                return;
//            }
//
//            // 4. 토큰이 유효하면 SecurityContext에 저장
//            String username = tokenProvider.getUsername(token);
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            UsernamePasswordAuthenticationToken auth =
//                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//
//        filterChain.doFilter(request, response);
//    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
