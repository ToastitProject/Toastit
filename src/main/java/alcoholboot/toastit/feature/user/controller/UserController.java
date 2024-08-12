package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.auth.jwt.domain.Token;
import alcoholboot.toastit.auth.jwt.service.TokenService;
import alcoholboot.toastit.auth.jwt.util.JwtTokenizer;
import alcoholboot.toastit.feature.user.controller.request.UserJoinRequest;
import alcoholboot.toastit.feature.user.controller.request.UserLoginRequest;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.exception.EmailVerificationException;
import alcoholboot.toastit.feature.user.service.UserService;
import alcoholboot.toastit.global.response.code.CommonExceptionCode;
import alcoholboot.toastit.global.response.exception.CustomException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("userLoginRequest", new UserLoginRequest());
        return "/feature/user/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute @Valid UserLoginRequest userLoginDto,
                        BindingResult bindingResult,
                        HttpServletResponse response,
                        Model model) {

        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> log.error("Validation error: {}", error.getDefaultMessage()));
            return "/feature/user/loginForm";
        }

        User user = userService.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new CustomException(CommonExceptionCode.NOT_MATCH_EMAILL_OR_PASSWORD));

        // 비밀번호 일치여부 체크
        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
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

        model.addAttribute("user", user);

        log.info("홈페이지로 이동!");

        return "redirect:/";
    }

    @DeleteMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = null;

        // access 및 refresh token cookie 삭제
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

        return "redirect:/feature/mainForm";
    }

    @GetMapping("/join")
    public String showJoinPage(Model model) {
        model.addAttribute("userJoinRequest", new UserJoinRequest());

        log.info("회원가입 폼 반환");

        return "/feature/user/joinForm";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute @Valid UserJoinRequest userJoinDto, BindingResult bindingResult, Model model) {
        // Model에 있는 모든 값 출력
//        log.info("Model Attributes:");
//        for (Map.Entry<String, Object> entry : model.asMap().entrySet()) {
//            log.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//        }

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> log.error("Validation error: {}", error.getDefaultMessage()));
            return "/feature/user/joinForm";
        }

        try {
            log.info("유저 저장 시작! 이메일: {}, 인증코드: {}", userJoinDto.getEmail(), userJoinDto.getAuthCode());
            userService.save(userJoinDto);
        } catch (EmailVerificationException e) {
            model.addAttribute("error", e.getMessage());
            return "/feature/user/joinForm";
        }

        return "redirect:/user/login";
    }
}