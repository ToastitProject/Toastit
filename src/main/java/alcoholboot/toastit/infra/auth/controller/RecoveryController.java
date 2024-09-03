package alcoholboot.toastit.infra.auth.controller;

import alcoholboot.toastit.feature.user.service.UserManagementService;
import alcoholboot.toastit.infra.email.service.EmailService;
import alcoholboot.toastit.infra.email.service.VerificationService;
import alcoholboot.toastit.infra.email.util.RandomAuthCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/recovery") // 기본 경로를 /recovery로 설정
@RequiredArgsConstructor
public class RecoveryController {

    private final UserManagementService userManagementService;
    private final VerificationService verificationService;
    private final EmailService emailService;

    /**
     * 비밀번호 찾기 페이지 보여주는 메소드
     */
    @GetMapping("/reset")
    public String showPasswordResetPage() {
        return "auth/password-reset-form"; // 타임리프 템플릿 파일 (password/reset.html)
    }

    /**
     * 비밀번호 찾기 이메일 발송 메소드
     */
    @PostMapping("/send")
    public String sendResetPasswordEmail(@RequestParam("email") String email, Model model) {

        log.debug(email + " 해당 이메일로 비밀번호 찾기 요청이 접수되었습니다.");

        // 사용자가 존재하는지 확인
        if (!userManagementService.existsByEmail(email)) {
            model.addAttribute("error", "해당 이메일로 등록된 계정을 찾을 수 없습니다.");
            return "auth/password-reset-form";
        }

        // 소셜 로그인 계정인지 확인
        if (userManagementService.isSocialLoginEmail(email)) {
            model.addAttribute("socialError", "해당 이메일은 소셜 로그인 계정으로 등록되어 있습니다. 로그인 페이지로 이동합니다.");
            return "auth/password-reset-form";
        }

        // 랜덤 인증 코드 생성
        String authCode = RandomAuthCode.generate();

        // redis에 인증 코드 저장
        verificationService.saveCode(email, authCode);

        log.debug("발급된 인증번호는 " + authCode + "입니다.");

        // 메일 제목
        String subject = "[ToastIT] 이메일 인증번호 : " + authCode;

        // 메일 본문
        String body = emailService.setAuthForm(authCode);

        // 메일 발송
        emailService.sendFormMail(email, subject, body);

        model.addAttribute("email", email);

        return "auth/password-verify-form"; // 인증번호 입력 페이지 (password/verify.html)
    }

    /**
     * 인증번호 검증 및 비밀번호 재설정 페이지로 이동하는 메소드
     */
    @PostMapping("/verify")
    public String verifyResetCode(@RequestParam("email") String email, @RequestParam("authCode") String authCode, Model model) {

        log.debug("이메일: " + email + ", 인증번호: " + authCode + " 확인 요청이 접수되었습니다.");

        // 인증번호 확인
        if (!verificationService.verifyCode(email, authCode)) {

            model.addAttribute("error", "인증 코드가 일치하지 않습니다.");
            model.addAttribute("email", email);

            return "auth/password-verify-form"; // 인증번호 입력 페이지 (password/verify.html)
        }

        // 인증 성공 시 비밀번호 재설정 페이지로 이동
        model.addAttribute("email", email);

        return "auth/new-password-form"; // 비밀번호 재설정 페이지 (password/new-password.html)
    }

    /**
     * 비밀번호 재설정 메소드
     */
    @PostMapping("/reset/password")
    public String resetPassword(@RequestParam("email") String email, @RequestParam("newPassword") String newPassword, Model model) {

        log.debug(email + " 사용자의 비밀번호 재설정 요청이 접수되었습니다.");

        // 사용자 비밀번호 변경
        userManagementService.updatePassword(email, newPassword);

        model.addAttribute("message", "비밀번호가 성공적으로 재설정되었습니다. 로그인 페이지로 이동합니다.");

        return "auth/password-reset-success"; // 비밀번호 재설정 성공 페이지 (password/reset-success.html)
    }
}