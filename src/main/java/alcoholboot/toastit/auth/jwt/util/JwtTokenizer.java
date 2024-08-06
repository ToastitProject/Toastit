package alcoholboot.toastit.auth.jwt.util;

import alcoholboot.toastit.feature.user.type.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * JWT 생성 & 검증 클래스
 */
@Component
public class JwtTokenizer {
    private final byte[] accessSecret;
    private final byte[] refreshSecret;
    public static Long accessTokenExpire;
    public static Long refreshTokenExpire;

    public JwtTokenizer(@Value("${jwt.accessSecret}") String accessSecret,
                        @Value("${jwt.refreshSecret}") String refreshSecret,
                        @Value("${jwt.accessTokenExpire}") Long accessTokenExpire,
                        @Value("${jwt.refreshTokenExpire}") Long refreshTokenExpire) {
        this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);
        this.accessTokenExpire = accessTokenExpire;
        this.refreshTokenExpire = refreshTokenExpire;
    }

    /**
     * AccessToken 생성
     *
     * @param id
     * @param email
     * @param nickname
     * @param authority
     * @return AccessToken
     */
    public String createAccessToken(Long id, String email, String nickname, Authority authority) {
        return createToken(id, email, nickname, authority, accessTokenExpire, accessSecret);
    }

    /**
     * RefreshToken 생성
     *
     * @param id
     * @param email
     * @param nickname
     * @param authority
     * @return RefreshToken
     */
    public String createRefreshToken(Long id, String email, String nickname, Authority authority) {
        return createToken(id, email, nickname, authority, refreshTokenExpire, refreshSecret);
    }

    /**
     * access token 파싱
     * @param accessToken access token
     * @return 파싱된 토큰
     */
    public Claims parseAccessToken(String accessToken) {
        return parseToken(accessToken, accessSecret);
    }

    /**
     * refresh token 파싱
     * @param refreshToken refresh token
     * @return 파싱된 토큰
     */
    public Claims parseRefreshToken(String refreshToken) {
        return parseToken(refreshToken, refreshSecret);
    }

    /**
     * Jwts Builder 사용하여 token 생성
     *
     * @param id
     * @param email
     * @param nickname
     * @param authority
     * @param expire
     * @param secretKey
     * @return
     */
    private String createToken(Long id, String email, String nickname, Authority authority, Long expire, byte[] secretKey) {
        // 기본으로 가지고 있는 Claims Subject -> email
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("authority", authority);
        claims.put("userId", id);
        claims.put("nickname", nickname);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expire))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    /**
     * token 파싱
     *
     * @param token access/refresh token
     * @param secretKey access/refresh 비밀키
     * @return 파싱된 토큰
     */
    private Claims parseToken(String token, byte[] secretKey) {
        Claims claims = null;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) { // 토큰 유효성 체크 실패 시
            throw new SignatureException("토큰이 유효하지 않습니다.", e);
        }

        return claims;
    }

    /**
     * @param secretKey - byte 형식
     * @return 해싱 Key 형식 시크릿 키
     */
    private static Key getSigningKey(byte[] secretKey) {
        return Keys.hmacShaKeyFor(secretKey);
    }
}