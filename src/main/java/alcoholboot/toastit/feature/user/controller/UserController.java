package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.auth.jwt.domain.Token;
import alcoholboot.toastit.auth.jwt.service.TokenService;
import alcoholboot.toastit.auth.jwt.util.JwtTokenizer;
import alcoholboot.toastit.feature.user.controller.request.EmailCheckRequest;
import alcoholboot.toastit.feature.user.controller.request.EmailSendRequest;
import alcoholboot.toastit.feature.user.controller.request.UserJoinRequest;
import alcoholboot.toastit.feature.user.controller.request.UserLoginRequest;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.exception.EmailVerificationException;
import alcoholboot.toastit.feature.user.service.EmailService;
import alcoholboot.toastit.feature.user.service.UserService;
import alcoholboot.toastit.feature.user.service.VerificationService;
import alcoholboot.toastit.feature.user.util.RandomAuthCode;
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
    private final EmailService emailService;
    private final VerificationService verificationService;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("userLoginRequest", new UserLoginRequest());
        return "/main/user/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute @Valid UserLoginRequest userLoginDto,
                        BindingResult bindingResult,
                        HttpServletResponse response,
                        Model model) {

        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            return "/main/user/loginForm";
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

        return "redirect:/mainForm";
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
        return "redirect:/";
    }

    @GetMapping("/join")
    public String showJoinPage(Model model) {
        model.addAttribute("userJoinRequest", new UserJoinRequest());
        return "/main/user/joinForm";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute @Valid UserJoinRequest userJoinDto, BindingResult bindingResult, Model model) {
        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            return "/main/user/joinForm";
        }

        try {
            userService.save(userJoinDto);
        } catch (EmailVerificationException e) {
            model.addAttribute("error", e.getMessage());
            return "/main/user/joinForm";
        }

        return "redirect:/main/user/loginForm";
    }

    /**
     * 인증번호 발송 메소드
     */
    @PostMapping("/authEmail")
    public String sendAuthEmail(@ModelAttribute @Valid EmailSendRequest emailSendDto, BindingResult bindingResult, Model model) {
        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            return "/main/user/joinForm";
        }

        // 랜덤 인증 코드 생성
        String authCode = RandomAuthCode.generate();

        // redis에 인증 코드 저장
        verificationService.saveCode(emailSendDto.getEmail(), authCode);

        // 메일 발송
        emailService.sendSimpleMessage(emailSendDto.getEmail(), "[술프링부트] 이메일 인증번호 발송드립니다.", authCode);
        model.addAttribute("sendEmail Message", "인증 메일이 발송되었습니다.");

        return "/main/user/joinForm";
    }

    /**
     * 인증번호 검증 메소드
     */
    @GetMapping("/authEmail")
    public String checkAuthEmail(@ModelAttribute @Valid EmailCheckRequest emailCheckDto, BindingResult bindingResult, Model model) {
        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            return "/user/joinForm";
        }

        // redis에 저장된 인증번호와 비교하여 확인
        if (!verificationService.verifyCode(emailCheckDto.getEmail(), emailCheckDto.getAuthCode())) {
            model.addAttribute("authCodeMismatchMessage", CommonExceptionCode.NOT_MATCH_AUTH_CODE.getData());
            return "/main/user/joinForm";
        }

        model.addAttribute("verifiedEmailMessage", "인증이 완료되었습니다.");
        return "/main/user/joinForm";
    }
}