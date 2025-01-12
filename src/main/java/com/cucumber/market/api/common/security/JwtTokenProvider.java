package com.cucumber.market.api.common.security;

import com.cucumber.market.api.mapper.user.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


import java.util.Date;

/**
 * jwt 생성, 검증 로직
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    @Value("${jwt.expired.time.accesstoken}")
    private long ACCESS_TOKEN_EXPIRED_TIME; // 1시간

    @Value("${jwt.expired.time.refreshtoken}")
    private long REFRESH_TOKEN_EXPIRED_TIME; // 7일

    @Autowired
    private UserMapper userMapper;

    //accessToken 생성
    public String generateAccessToken(Integer memberId) {
        Claims claims = Jwts.claims();
        claims.put("memberId",memberId);

        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRED_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    //refreshToken 생성
    public String generateRefreshToken() {
        Claims claims = Jwts.claims();

        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRED_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

    }

    public boolean validateAccessToken(String accessToken) {
        try {
            accessToken = accessToken.replace("Bearer ", "");
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(accessToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            // "Bearer " 접두어 제거
            refreshToken = refreshToken.replace("Bearer ", "");
            // 토큰 유효한지 체크
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(refreshToken);

            // DB에서 한번 더 체크
            String validToken = userMapper.selectRefreshToken(refreshToken);

            if(validToken != null){
                return true;
            } else {
                return false;
            }

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //accessToken 에서 멤버 ID 가져오기
    public Integer getMemberIdFromAccessToken(String accessToken) {
        // "Bearer " 접두어 제거
        accessToken = accessToken.replace("Bearer ", "");
        String memberId = (String) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken).getBody().get("memberId");
        return Integer.parseInt(memberId);
    }

    //refreshToken 에서 멤버 ID 가져오기
    public Integer getMemberIdFromRefreshToken(String refreshToken) {
        // "Bearer " 접두어 제거
        refreshToken = refreshToken.replace("Bearer ", "");
        Integer memberId = Integer.valueOf(userMapper.selectMemberIdRefreshToken(refreshToken));

        return memberId;
    }
}
