package alcoholboot.toastit.infra.jwt.service.Impl;

import alcoholboot.toastit.infra.jwt.service.TokenRenewalService;
import alcoholboot.toastit.infra.jwt.service.TokenService;
import alcoholboot.toastit.infra.jwt.util.JwtTokenizer;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.service.UserManagementService;
import alcoholboot.toastit.feature.user.type.Authority;
import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;
import com.amazonaws.services.kms.model.NotFoundException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * {@link TokenRenewalService} 인터페이스 구현 클래스
 * 만료된 액세스 토큰을 리프레시 토큰을 사용하여 재발급하는 기능을 제공한다.
 */
@Service
@RequiredArgsConstructor
public class TokenRenewalServiceImpl implements TokenRenewalService {

    // JWT 토큰 생성 및 파싱 도구
    private final JwtTokenizer jwtTokenizer;

    // 사용자 관련 서비스
    private final UserManagementService userManagementService;

    // 토큰 관련 서비스
    private final TokenService tokenService;

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 생성하고,
     * 응답 쿠키에 추가하여 반환.
     *
     * @param response     응답 객체에 액세스 토큰을 담은 쿠키를 추가
     * @param refreshToken 클라이언트가 보유한 리프레시 토큰
     * @return 새롭게 발급된 액세스 토큰
     */
    @Override
    public String refreshAccessToken(HttpServletResponse response, String refreshToken) {
        // 리프레시 토큰을 파싱하여 클레임을 가져옴
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);

        // 클레임에서 사용자 ID를 추출하고, 해당 사용자 정보를 조회
        Long userId = Long.valueOf((Integer) claims.get("userId"));
        User user = userManagementService.findById(userId).orElseThrow(() -> new NotFoundException(CommonExceptionCode.NOT_FOUND_USER.getData()));
        Authority authority = Authority.valueOf(claims.get("authority", String.class));

        // 사용자 정보를 바탕으로 새로운 액세스 토큰을 생성
        String accessToken = jwtTokenizer.createAccessToken(userId, user.getEmail(), user.getNickname(), authority);

        // 기존 리프레시 토큰에 해당하는 액세스 토큰 정보를 업데이트
        tokenService.updateByRefreshToken(refreshToken, accessToken);

        // 액세스 토큰을 담은 쿠키를 생성하고 응답에 추가
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.accessTokenExpire / 1000));
        response.addCookie(accessTokenCookie);

        // 새로 발급된 액세스 토큰을 반환
        return accessToken;
    }
}