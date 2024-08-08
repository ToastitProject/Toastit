package alcoholboot.toastit.auth.jwt.service.Impl;

import alcoholboot.toastit.auth.jwt.service.TokenRenewalService;
import alcoholboot.toastit.auth.jwt.service.TokenService;
import alcoholboot.toastit.auth.jwt.util.JwtTokenizer;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.service.UserService;
import alcoholboot.toastit.feature.user.type.Authority;
import com.amazonaws.services.kms.model.NotFoundException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenRenewalServiceImpl implements TokenRenewalService {
    private final JwtTokenizer jwtTokenizer;
    private final UserService userService;
    private final TokenService tokenService;

    @Override
    public void refreshAccessToken(HttpServletResponse response, String refreshToken) {
        // refresh token 파싱
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);

        // 유저 정보 가져오기
        Long userId = Long.valueOf((Integer) claims.get("userId"));
        User user = userService.findById(userId).orElseThrow(() -> new NotFoundException("해당 ID에 해당하는 회원 정보를 찾을 수 없습니다."));
        Authority authority = Authority.valueOf(claims.get("authority", String.class));

        // access token 생성
        String accessToken = jwtTokenizer.createAccessToken(userId, user.getEmail(), user.getNickname(), authority);

        // accessToken 쿠키 생성
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.accessTokenExpire / 1000));

        // 응답에 쿠키 추가
        response.addCookie(accessTokenCookie);

        // Token 데이터 access token 값 업데이트
        tokenService.updateByRefreshToken(refreshToken, accessToken);
    }
}