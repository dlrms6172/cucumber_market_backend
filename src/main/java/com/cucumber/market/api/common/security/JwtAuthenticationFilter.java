package com.cucumber.market.api.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * jwt가 컨트롤러로 가기전에 유효한지 검증하는 필터
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //허용된 URI인지 확인 (SecurityConfig와 동일한 URI 목록)
        String requestURI = request.getRequestURI();

        if (permittedURI(requestURI)) {
            filterChain.doFilter(request, response);
            System.out.println(requestURI);
            return;
        }

        try {
            String token = resolveToken(request);

            if (token == null || !jwtTokenProvider.validateAccessToken(token)) {
                throw new SecurityException("Invalid or missing JWT token");
            }

            // 토큰이 유효한 경우 인증 객체 설정
            Integer memberId = jwtTokenProvider.getMemberIdFromAccessToken(token);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(memberId, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 다음 필터로 진행
            filterChain.doFilter(request, response);

        } catch (SecurityException e) {
            // JWT 예외 처리
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"resultCode\": \"401\", \"resultMsg\": \"" + e.getMessage() + "\"}");
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // SecurityConfig에서 설정된 허용 URI와 동일하게 체크
    private boolean permittedURI(String requestURI) {
        return requestURI.startsWith("/user/signin") || requestURI.equals("/");
    }
}
