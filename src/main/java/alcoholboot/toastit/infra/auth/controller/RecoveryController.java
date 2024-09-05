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

/**
 * 비밀번호 복구와 관련된 요청을 처리하는 컨트롤러.
 */
@Slf4j
@Controller
@RequestMapping("/recovery")
@RequiredArgsConstructor
public class RecoveryController {

    private final UserManagementService userManagementService;
    private final VerificationService verificationService;
    private final EmailService emailService;

    /**
     * 비밀번호 재설정 페이지를 보여줍니다.
     *
     * @return 비밀번호 재설정 폼 뷰
     */
    @GetMapping("/reset")
    public String showPasswordResetPage() {
        return "auth/password-reset-form";
    }

    /**
     * 비밀번호 재설정을 위한 이메일을 발송합니다.
     *
     * @param email 비밀번호 재설정을 요청한 사용자 이메일
     * @param model 에러 메시지 또는 성공 메시지를 담을 모델
     * @return 인증번호 입력 페이지 뷰
     */
    @PostMapping("/send")
    public String sendResetPasswordEmail(@RequestParam("email") String email, Model model) {
        log.debug(email + " 해당 이메일로 비밀번호 찾기 요청이 접수되었습니다.");

        if (!userManagementService.existsByEmail(email)) {
            model.addAttribute("error", "해당 이메일로 등록된 계정을 찾을 수 없습니다.");
            return "auth/password-reset-form";
        }

        if (userManagementService.isSocialLoginEmail(email)) {
            model.addAttribute("socialError", "해당 이메일은 소셜 로그인 계정으로 등록되어 있습니다.");
            return "auth/password-reset-form";
        }

        String authCode = RandomAuthCode.generate();
        verificationService.saveCode(email, authCode);
        log.debug("발급된 인증번호는 " + authCode + "입니다.");

        String subject = "[ToastIT] 이메일 인증번호 : " + authCode;
        String body = emailService.setAuthForm(authCode);

        emailService.sendFormMail(email, subject, body);

        model.addAttribute("email", email);
        return "auth/password-verify-form";
    }

    /**
     * 인증번호 검증 후 비밀번호 재설정 페이지로 이동합니다.
     *
     * @param email 비밀번호 재설정을 요청한 사용자 이메일
     * @param authCode 사용자가 입력한 인증번호
     * @param model 에러 메시지 또는 성공 메시지를 담을 모델
     * @return 비밀번호 재설정 페이지 뷰
     */
    @PostMapping("/verify")
    public String verifyResetCode(@RequestParam("email") String email, @RequestParam("authCode") String authCode, Model model) {
        log.debug("이메일: " + email + ", 인증번호: " + authCode + " 확인 요청이 접수되었습니다.");

        if (!verificationService.verifyCode(email, authCode)) {
            model.addAttribute("error", "인증 코드가 일치하지 않습니다.");
            model.addAttribute("email", email);
            return "auth/password-verify-form";
        }

        model.addAttribute("email", email);
        return "auth/new-password-form";
    }

    /**
     * 사용자의 비밀번호를 재설정합니다.
     *
     * @param email 비밀번호 재설정을 요청한 사용자 이메일
     * @param newPassword 새로 설정할 비밀번호
     * @param model 성공 메시지를 담을 모델
     * @return 비밀번호 재설정 성공 페이지 뷰
     */
    @PostMapping("/reset/password")
    public String resetPassword(@RequestParam("email") String email, @RequestParam("newPassword") String newPassword, Model model) {
        log.debug(email + " 사용자의 비밀번호 재설정 요청이 접수되었습니다.");

        userManagementService.updatePassword(email, newPassword);
        model.addAttribute("message", "비밀번호가 성공적으로 재설정되었습니다.");

        return "auth/password-reset-success";
    }
}