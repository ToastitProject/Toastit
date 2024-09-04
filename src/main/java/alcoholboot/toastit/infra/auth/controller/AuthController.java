package alcoholboot.toastit.infra.auth.controller;

import alcoholboot.toastit.infra.auth.controller.request.AuthJoinRequest;
import alcoholboot.toastit.infra.auth.controller.request.AuthLoginRequest;
import alcoholboot.toastit.infra.auth.service.AuthService;
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

/**
 * 인증 관련 요청을 처리하는 컨트롤러.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * 로그인 페이지를 보여줍니다.
     *
     * @param model 뷰에 데이터를 전달하기 위한 모델
     * @return 로그인 폼 뷰 이름
     */
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("authLoginRequest", new AuthLoginRequest());
        log.debug("로그인 페이지가 요청되었습니다.");
        return "auth/login-form";
    }

    /**
     * 로그인 처리를 수행합니다.
     *
     * @param authLoginRequest 로그인 요청 데이터
     * @param bindingResult 유효성 검증 결과
     * @param response HTTP 응답 객체
     * @param model 뷰에 데이터를 전달하기 위한 모델
     * @return 성공 시 리다이렉트, 실패 시 로그인 폼 뷰
     */
    @PostMapping("/login")
    public String login(@ModelAttribute @Valid AuthLoginRequest authLoginRequest,
                        BindingResult bindingResult,
                        HttpServletResponse response,
                        Model model) {
        log.debug("로그인 시도: 이메일 {}", authLoginRequest.getEmail());

        if (bindingResult.hasErrors()) {
            return "auth/login-form";
        }

        User user = authService.login(authLoginRequest, response);
        model.addAttribute("user", user);

        log.debug("로그인 성공: 이메일 {}", user.getEmail());
        return "redirect:/";
    }

    /**
     * 로그아웃 처리를 수행합니다.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @return 메인 페이지로 리다이렉트
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        log.debug("로그아웃이 요청되었습니다.");
        authService.logout(request, response);
        log.debug("로그아웃이 성공적으로 처리되었습니다.");
        return "redirect:/";
    }

    /**
     * 회원가입 페이지를 보여줍니다.
     *
     * @param model 뷰에 데이터를 전달하기 위한 모델
     * @return 회원가입 폼 뷰 이름
     */
    @GetMapping("/join")
    public String showJoinPage(Model model) {
        model.addAttribute("authJoinRequest", new AuthJoinRequest());
        log.debug("회원가입 페이지가 요청되었습니다.");
        return "auth/join-form";
    }

    /**
     * 회원가입 처리를 수행합니다.
     *
     * @param userJoinDto 회원가입 요청 데이터
     * @param bindingResult 유효성 검증 결과
     * @param model 뷰에 데이터를 전달하기 위한 모델
     * @return 성공 시 로그인 페이지로 리다이렉트, 실패 시 회원가입 폼 뷰
     */
    @PostMapping("/join")
    public String join(@ModelAttribute @Valid AuthJoinRequest userJoinDto, BindingResult bindingResult, Model model) {
        log.debug("회원가입 시도: 이메일 {}", userJoinDto.getEmail());

        try {
            authService.registerUser(userJoinDto, bindingResult);
        } catch (Exception e) {
            log.error("회원가입 중 오류가 발생했습니다: {}", e.getMessage());
            return "auth/join-form";
        }

        log.debug("회원가입 성공: 이메일 {}", userJoinDto.getEmail());
        return "redirect:/auth/login";
    }

    /**
     * 회원 탈퇴 폼 페이지를 보여줍니다.
     *
     * @param model 뷰에 데이터를 전달하기 위한 모델
     * @return 회원 탈퇴 폼 뷰 이름
     */
    @GetMapping("/resign")
    public String resign(Model model) {
        log.debug("회원 탈퇴 폼 페이지가 요청되었습니다.");
        User user = authService.getResignUser();
        model.addAttribute("user", user);
        return "auth/resign-form";
    }

    /**
     * 회원 탈퇴 처리를 수행합니다.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @return 메인 페이지로 리다이렉트
     */
    @PostMapping("/resign")
    public String resign(HttpServletRequest request, HttpServletResponse response) {
        log.debug("회원 탈퇴가 요청되었습니다.");
        authService.resignUser(request, response);
        log.debug("회원 탈퇴가 성공적으로 처리되었습니다.");
        return "redirect:/";
    }
}