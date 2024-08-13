package alcoholboot.toastit.auth.jwt.filter;

import alcoholboot.toastit.auth.jwt.info.CustomUserDetails;
import alcoholboot.toastit.auth.jwt.service.TokenRenewalService;
import alcoholboot.toastit.auth.jwt.util.JwtTokenizer;
import alcoholboot.toastit.auth.jwt.token.JwtAuthenticationToken;
import alcoholboot.toastit.feature.user.type.Authority;
import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;
import alcoholboot.toastit.global.config.response.exception.CustomException;
import com.amazonaws.services.kms.model.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * JWT 토큰을 사용하여 각 요청을 인증하는 필터 클래스.
 */
@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final TokenRenewalService tokenRenewalService;

    /**
     * 필터 메서드
     * 각 요청마다 JWT 토큰을 검증하고 인증을 설정
     *
     * @param request     요청 객체
     * @param response    응답 객체
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException      입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청에서 액세스 토큰 추출
        String accessToken = getToken(request, "accessToken");

        if (StringUtils.hasText(accessToken)) {
            try {
                log.info("[AUTH START] : " + accessToken);
                getAuthentication(accessToken); // 토큰을 사용하여 인증 요청
            } catch (ExpiredJwtException ea) {
                log.warn("[TOKEN EXPIRED] - 액세스 토큰이 만료되었습니다.");

                // 요청에서 리프레쉬 토큰 추출
                String refreshToken = getToken(request, "refreshToken");

                log.info("토큰 재발행을 시도합니다." + refreshToken);

                if (refreshToken == null) {
                    log.error("[TOKEN MISSING] - " + CommonExceptionCode.JWT_UNKNOWN_ERROR.getData());
                    throw new CustomException(CommonExceptionCode.TIMEOUT_LOGOUT);
                } else {
                    try {
                        String newAccessToken = tokenRenewalService.refreshAccessToken(response, refreshToken);

                        log.info("[AUTH START] : " + newAccessToken);
                        getAuthentication(accessToken);
                    } catch (ExpiredJwtException er) {
                        log.error("[TOKEN EXPIRED] - " + CommonExceptionCode.JWT_EXPIRED_ERROR.getData());
                        throw new CustomException(CommonExceptionCode.TIMEOUT_LOGOUT);
                    } catch (NotFoundException en) {
                        log.error("[NOT_FOUND] - " + CommonExceptionCode.NOT_FOUND.getData());
                        throw new CustomException(CommonExceptionCode.TIMEOUT_LOGOUT);
                    }
                }
            } catch (UnsupportedJwtException e) {
                log.error("[UNSUPPORTED TOKEN] - " + CommonExceptionCode.JWT_UNSUPPORTED_ERROR.getData());
                throw new CustomException(CommonExceptionCode.TIMEOUT_LOGOUT);
            } catch (MalformedJwtException e) {
                log.error("[INVALID TOKEN] - " + CommonExceptionCode.JWT_INVALID_ERROR.getData());
                throw new CustomException(CommonExceptionCode.TIMEOUT_LOGOUT);
            } catch (IllegalArgumentException e) {
                log.error("[TOKEN DECRYPTION ERROR] - " + CommonExceptionCode.JWT_DECRYPTION_ERROR.getData());
            } catch (Exception e) {
                log.error("[INTERNAL ERROR] - " + CommonExceptionCode.JWT_INTERNAL_ERROR.getData());
                throw new CustomException(CommonExceptionCode.TIMEOUT_LOGOUT);
            }
        }

        filterChain.doFilter(request, response); // 다음 필터로 요청을 전달
    }

    /**
     * 토큰을 사용하여 인증 설정
     *
     * @param token JWT 토큰
     */
    private void getAuthentication(String token) {
        // 토큰에서 클레임 파싱
        Claims claims = jwtTokenizer.parseAccessToken(token); // 토큰에서 클레임을 파싱

        // 이메일 ID 추출
        String email = claims.getSubject(); // 이메일을 가져옴

        // 사용자 ID 추출
        Long userId = claims.get("userId", Long.class);

        // 사용자 닉네임 추출
        String nickname = claims.get("nickname", String.class);

        // 사용자 권한 추출
        Authority authority = Authority.valueOf(claims.get("authority", String.class));

        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);

        // 인증 주체 선언
        CustomUserDetails userDetails = new CustomUserDetails(email, "", nickname, (List<GrantedAuthority>) authorities);

        // 인증 객체 생성
        Authentication authentication = new JwtAuthenticationToken(authorities, userDetails, null);

        // 인증 객체 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 요청에서 토큰을 추출
     *
     * @param request   요청 객체
     * @param tokenName 토큰 이름
     * @return JWT 토큰
     */
    private String getToken(HttpServletRequest request, String tokenName) {
        // 요청에서 쿠키 추출
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenName.equals(cookie.getName())) {
                    return cookie.getValue(); // 쿠키에서 토큰 반환
                }
            }
        }

        return null; // 토큰을 찾지 못한 경우 null 반환
    }
}