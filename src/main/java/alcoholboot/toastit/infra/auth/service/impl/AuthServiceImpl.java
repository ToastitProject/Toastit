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

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserManagementService userManagementService;
    private final TokenService tokenService;

    private final JwtTokenizer jwtTokenizer;
    private final PasswordEncoder passwordEncoder;

    public User login(AuthLoginRequest authLoginRequest, HttpServletResponse response) {

        // 이메일로 사용자 조회
        User user = userManagementService.findByEmail(authLoginRequest.getEmail())
                .orElseThrow(() -> new CustomException(CommonExceptionCode.NOT_MATCH_EMAILL_OR_PASSWORD));

        // 비밀번호 일치 여부 체크
        if (!passwordEncoder.matches(authLoginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(CommonExceptionCode.NOT_MATCH_EMAILL_OR_PASSWORD);
        }

        // 액세스 토큰 발급
        String accessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getNickname(), user.getAuthority());

        // 리프레쉬 토큰 발급
        String refreshToken = jwtTokenizer.createRefreshToken(user.getId(), user.getEmail(), user.getNickname(), user.getAuthority());

        // 리프레시 토큰 디비 저장
        Token token = Token.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType("Bearer")
                .build();

        tokenService.saveOrUpdate(token);

        // 액세스 토큰 쿠키 생성 및 저장
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.accessTokenExpire / 1000));
        response.addCookie(accessTokenCookie);

        // 리프레쉬 토큰 쿠키 생성 및 저장
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.refreshTokenExpire / 1000));
        response.addCookie(refreshTokenCookie);

        return user;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = null;

        // 쿠키에서 access 및 refresh token 삭제
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

        // tokens 데이터 삭제
        tokenService.deleteByAccessToken(accessToken);
    }

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

    public User getResignUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userManagementService.findByEmail(email)
                .orElseThrow(() -> new CustomException(CommonExceptionCode.NOT_FOUND_USER));
    }

    public void resignUser(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        log.debug("접속한 사용자의 이메일 : " + email);

        User user = userManagementService.findByEmail(email)
                .orElseThrow(() -> new CustomException(CommonExceptionCode.NOT_FOUND_USER));

        String accessToken = null;

        // 쿠키에서 access 및 refresh token 삭제
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