package alcoholboot.toastit.auth.controller;

import alcoholboot.toastit.auth.controller.request.AuthJoinRequest;
import alcoholboot.toastit.auth.controller.request.AuthLoginRequest;
import alcoholboot.toastit.auth.service.AuthService;
import alcoholboot.toastit.feature.user.domain.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("authLoginRequest", new AuthLoginRequest());

        log.debug("로그인 페이지가 요청되었습니다.");

        return "auth/login-form";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute @Valid AuthLoginRequest authLoginRequest,
                        BindingResult bindingResult,
                        HttpServletResponse response,
                        Model model) {

        log.debug("로그인 시도: 이메일 {}", authLoginRequest.getEmail());

        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            return "auth/login-form";
        }

        User user = authService.login(authLoginRequest, response);
        model.addAttribute("user", user);

        log.debug("로그인 성공: 이메일 {}", user.getEmail());

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        log.debug("로그아웃이 요청되었습니다.");

        authService.logout(request, response);

        log.debug("로그아웃이 성공적으로 처리되어 메인 페이지로 리다이렉트됩니다.");

        return "redirect:/";
    }

    @GetMapping("/join")
    public String showJoinPage(Model model) {
        model.addAttribute("authJoinRequest", new AuthJoinRequest());

        log.debug("회원가입 페이지가 요청되었습니다.");

        return "auth/join-form";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute @Valid AuthJoinRequest userJoinDto, BindingResult bindingResult, Model model) {
        log.debug("회원가입 시도: 이메일 {}", userJoinDto.getEmail());

        try {
            authService.registerUser(userJoinDto, bindingResult);
        } catch (Exception e) {
            log.error("회원가입 중 오류가 발생했습니다: {}", e.getMessage());
            return "auth/join-form";
        }

        log.debug("회원가입이 성공적으로 처리되었습니다: 이메일 {}", userJoinDto.getEmail());

        return "redirect:/auth/login";
    }

    @GetMapping("/resign")
    public String resign() {
        log.debug("회원 탈퇴 폼 페이지가 요청되었습니다.");

        User user = authService.getResignUser();

        log.debug("회원 탈퇴 폼 페이지가 반환되었습니다: 이메일 {}", user.getEmail());

        return "auth/resign-form";
    }

    @PostMapping("/resign")
    public String resign(HttpServletRequest request, HttpServletResponse response) {
        log.debug("회원 탈퇴가 요청되었습니다.");

        authService.resignUser(request, response);

        log.debug("회원 탈퇴가 성공적으로 처리되어 메인 페이지로 리다이렉트됩니다.");

        return "redirect:/";
    }
}