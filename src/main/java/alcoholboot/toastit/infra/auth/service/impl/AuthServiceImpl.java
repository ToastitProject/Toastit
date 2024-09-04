package alcoholboot.toastit.infra.auth.service.impl;

import alcoholboot.toastit.infra.auth.controller.request.AuthJoinRequest;
import alcoholboot.toastit.infra.auth.controller.request.AuthLoginRequest;
import alcoholboot.toastit.infra.jwt.domain.Token;
import alcoholboot.toastit.infra.jwt.service.TokenService;
import alcoholboot.toastit.infra.jwt.util.JwtTokenizer;
import alcoholboot.toastit.infra.auth.service.AuthService;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.service.UserManagementService;
import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;
import alcoholboot.toastit.global.config.response.exception.CustomException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 인증 서비스 구현체.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserManagementService userManagementService;
    private final TokenService tokenService;
    private final JwtTokenizer jwtTokenizer;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자가 로그인하면 액세스 및 리프레시 토큰을 생성하고 쿠키에 저장합니다.
     *
     * @param authLoginRequest 로그인 요청 데이터
     * @param response HTTP 응답 객체
     * @return 로그인한 사용자 객체
     */
    public User login(AuthLoginRequest authLoginRequest, HttpServletResponse response) {
        User user = userManagementService.findByEmail(authLoginRequest.getEmail())
                .orElseThrow(() -> new CustomException(CommonExceptionCode.NOT_MATCH_EMAILL_OR_PASSWORD));

        if (!passwordEncoder.matches(authLoginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(CommonExceptionCode.NOT_MATCH_EMAILL_OR_PASSWORD);
        }

        String accessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getNickname(), user.getAuthority());
        String refreshToken = jwtTokenizer.createRefreshToken(user.getId(), user.getEmail(), user.getNickname(), user.getAuthority());

        Token token = Token.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType("Bearer")
                .build();
        tokenService.saveOrUpdate(token);

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.accessTokenExpire / 1000));
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.refreshTokenExpire / 1000));
        response.addCookie(refreshTokenCookie);

        return user;
    }

    /**
     * 사용자가 로그아웃할 때 액세스 및 리프레시 토큰을 쿠키에서 삭제하고, 토큰 저장소에서 삭제합니다.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case "accessToken":
                        accessToken = cookie.getValue();
                    case "refreshToken":
                        cookie.setValue("");
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        break;
                }
            }
        }

        tokenService.deleteByAccessToken(accessToken);
    }

    /**
     * 사용자를 회원가입합니다. 유효성 검증 오류가 발생하면 예외를 던집니다.
     *
     * @param authJoinRequest 회원가입 요청 데이터
     * @param bindingResult 유효성 검증 결과
     */
    public void registerUser(AuthJoinRequest authJoinRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getGlobalErrors().forEach(error -> log.error("GLOBAL ERROR : {}", error.getDefaultMessage()));
            bindingResult.getFieldErrors().forEach(error -> log.error("{} : {}", error.getField(), error.getDefaultMessage()));
            throw new IllegalArgumentException("Validation errors occurred during user registration");
        }

        log.info("유저 저장 시작! 이메일: {}, 인증코드: {}", authJoinRequest.getEmail(), authJoinRequest.getAuthCode());
        userManagementService.save(authJoinRequest);
        log.info("유저 저장 성공!");
    }

    /**
     * 탈퇴 요청 사용자를 조회하여 반환합니다.
     *
     * @return 탈퇴 요청 사용자 객체
     */
    public User getResignUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userManagementService.findByEmail(email)
                .orElseThrow(() -> new CustomException(CommonExceptionCode.NOT_FOUND_USER));
    }

    /**
     * 사용자의 회원 탈퇴를 처리하고 관련된 토큰을 삭제합니다.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     */
    public void resignUser(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.debug("접속한 사용자의 이메일 : " + email);

        User user = userManagementService.findByEmail(email)
                .orElseThrow(() -> new CustomException(CommonExceptionCode.NOT_FOUND_USER));

        String accessToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case "accessToken":
                        accessToken = cookie.getValue();
                    case "refreshToken":
                        cookie.setValue("");
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        break;
                }
            }
        }

        if (accessToken != null) {
            tokenService.deleteByAccessToken(accessToken);
        }

        userManagementService.deleteByEmail(email);
        log.debug("회원 탈퇴 완료");
    }
}