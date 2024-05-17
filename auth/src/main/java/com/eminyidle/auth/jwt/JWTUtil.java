package com.eminyidle.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * jwt 발급 jwt 검증
 */

@Component
public class JWTUtil {

    private final Key key;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {

        byte[] byteSecretKey = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(byteSecretKey); // 객체 key 생성
    }

    // username 검증
    public String getUserId(String token) {
        //sigingkey 부분이 유효성 검증하는 부분
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
                .get("userId", String.class);
    }

    // 남은 시간 가져오기
    public Long getExpiredDate(String refreshToken) {
        Date expiredate = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken)
                .getBody().getExpiration();
        Date now = new Date();
        return expiredate.getTime() - now.getTime();
    }

    // 토큰 유효 확인
    public Boolean isValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 생성
    public String createJwt(String userId, Long expiredMs) {

        Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(key, SignatureAlgorithm.HS256) //암호화
                .compact();
    }

    // 토큰 재생성
    public String regenerateRefreshJwt(String userId, String refreshToken, Long expiredMs,
                                       Long criteriaMs) {
        Date expiredate = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken)
                .getBody().getExpiration();
        Date now = new Date();
        if (expiredate.getTime() - now.getTime() < criteriaMs) {
            refreshToken = createJwt(userId, expiredMs);
        }

        return refreshToken;
    }
}