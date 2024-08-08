package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.auth.jwt.domain.Token;
import alcoholboot.toastit.auth.jwt.service.TokenService;
import alcoholboot.toastit.auth.jwt.util.JwtTokenizer;
import alcoholboot.toastit.feature.user.controller.request.EmailCheckRequest;
import alcoholboot.toastit.feature.user.controller.request.EmailSendRequest;
import alcoholboot.toastit.feature.user.controller.request.UserJoinRequest;
import alcoholboot.toastit.feature.user.controller.request.UserLoginRequest;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.service.EmailService;
import alcoholboot.toastit.feature.user.service.UserService;
import alcoholboot.toastit.feature.user.service.VerificationService;
import alcoholboot.toastit.feature.user.util.RandomAuthCode;
import com.amazonaws.services.kms.model.NotFoundException;
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
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute @Valid UserLoginRequest userLoginRequest, BindingResult bindingResult, HttpServletResponse httpServletResponse, Model model) {
        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "");
            return "login";
        }

        User user = userService.findByEmail(userLoginRequest.getEmail()).orElseThrow(() -> new NotFoundException(""));

        // 비밀번호 일치여부 체크
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            model.addAttribute("error", "");
            return "login";
        }

        // 토큰 발급
        String accessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getNickname(), user.getAuthority());
        String refreshToken = jwtTokenizer.createRefreshToken(user.getId(), user.getEmail(), user.getNickname(), user.getAuthority());

        // 리프레시 토큰 디비 저장
        Token token = Token.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType("Bearer")
                .build();

        tokenService.saveOrUpdate(token);

        // 토큰 쿠키 저장
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.accessTokenExpire / 1000));
        httpServletResponse.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.refreshTokenExpire / 1000));
        httpServletResponse.addCookie(refreshTokenCookie);

        model.addAttribute("user", user);
        return "redirect:/user/home";
    }

    @DeleteMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = null;

        // access / refresh token cookie 삭제
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
        return "redirect:/user/login";
    }

    @GetMapping("/join")
    public String showJoinPage(Model model) {
        model.addAttribute("userJoinRequest", new UserJoinRequest());
        return "join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute @Valid UserJoinRequest userJoinDto, BindingResult bindingResult, Model model) {
        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "");
            return "join";
        }

        userService.save(userJoinDto);
        return "redirect:/user/login";
    }

    /**
     * 인증번호 발송 메소드
     */
    @PostMapping("/authEmail")
    public String sendAuthEmail(@ModelAttribute @Valid EmailSendRequest emailSendDto, BindingResult bindingResult, Model model) {
        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "");
            return "authEmail";
        }

        // 랜덤 인증 코드 생성
        String authCode = RandomAuthCode.generate();
        // redis에 인증 코드 저장
        verificationService.saveCode(emailSendDto.getEmail(), authCode);
        // 메일 발송
        emailService.sendSimpleMessage(emailSendDto.getEmail(), "test", authCode);
        model.addAttribute("message", "인증 메일이 발송되었습니다.");
        return "authEmail";
    }

    /**
     * 인증번호 검증 메소드
     */
    @GetMapping("/authEmail")
    public String checkAuthEmail(@ModelAttribute @Valid EmailCheckRequest emailCheckDto, BindingResult bindingResult, Model model) {
        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "");
            return "checkAuthEmail";
        }

        // redis에 저장된 인증번호와 비교하여 확인
        if (!verificationService.verifyCode(emailCheckDto.getEmail(), emailCheckDto.getAuthCode())) {
            model.addAttribute("error", "");
            return "checkAuthEmail";
        }

        model.addAttribute("message", "인증이 완료되었습니다.");
        return "checkAuthEmail";
    }
}