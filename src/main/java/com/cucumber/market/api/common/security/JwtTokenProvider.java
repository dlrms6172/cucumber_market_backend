package com.cucumber.market.api.common.security;

import com.cucumber.market.api.mapper.user.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.Date;

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

    /**
     * JWT 토큰 생성
     */
    public String generateAccessToken(String memberId) {
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

    public String generateRefreshToken(String memberId) {
        Claims claims = Jwts.claims();
        claims.put("memberId",memberId);

        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRED_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        String cleanToken = refreshToken.replace("Bearer ", "").trim();
        String validToken = userMapper.selectRefreshToken(cleanToken);

        if(validToken != null){
            return true;
        } else {
            return false;
        }
    }

    public String getMemberId(String token) {
        return (String) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("memberId");
    }
}
