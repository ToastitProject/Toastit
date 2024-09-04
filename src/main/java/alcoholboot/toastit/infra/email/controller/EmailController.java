package alcoholboot.toastit.infra.email.controller;

import alcoholboot.toastit.feature.user.service.UserManagementService;
import alcoholboot.toastit.infra.email.controller.request.EmailCheckRequest;
import alcoholboot.toastit.infra.email.controller.request.EmailSendRequest;
import alcoholboot.toastit.infra.email.service.EmailService;
import alcoholboot.toastit.infra.email.service.VerificationService;
import alcoholboot.toastit.infra.email.util.RandomAuthCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 이메일 인증 관련 요청을 처리하는 컨트롤러.
 */
@Slf4j
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final UserManagementService userManagementService;
    private final VerificationService verificationService;
    private final EmailService emailService;

    /**
     * 인증번호를 이메일로 발송합니다.
     *
     * @param emailSendRequest 이메일 전송 요청 데이터
     * @param bindingResult 유효성 검증 결과
     * @return 성공 메시지 또는 오류 메시지를 포함한 응답 객체
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendAuthEmail(@RequestBody @Valid EmailSendRequest emailSendRequest, BindingResult bindingResult) {
        log.debug(emailSendRequest.getEmail() + " 해당 이메일이 요청되었습니다.");

        if (bindingResult.hasErrors()) {
            log.error("잘못된 이메일 형식이 전달되었습니다.");
            return ResponseEntity.badRequest().body("이메일 형식이 잘못되었습니다.");
        }

        if (userManagementService.existsByEmail(emailSendRequest.getEmail())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 이메일입니다.");
        }

        String authCode = RandomAuthCode.generate();
        verificationService.saveCode(emailSendRequest.getEmail(), authCode);
        log.debug("발급된 인증번호는 " + authCode + "입니다.");

        String subject = "[ToastIT] 이메일 인증번호 : " + authCode;
        String body = emailService.setAuthForm(authCode);

        emailService.sendFormMail(emailSendRequest.getEmail(), subject, body);
        log.debug(emailSendRequest.getEmail() + " 해당 이메일로 인증코드가 발송되었습니다.");
        return ResponseEntity.ok("인증 메일이 발송되었습니다.");
    }

    /**
     * 이메일로 발송된 인증번호를 검증합니다.
     *
     * @param emailCheckRequest 이메일 인증번호 확인 요청 데이터
     * @param bindingResult 유효성 검증 결과
     * @return 성공 메시지 또는 오류 메시지를 포함한 응답 객체
     */
    @PostMapping("/verify")
    public ResponseEntity<String> checkAuthEmail(@RequestBody @Valid EmailCheckRequest emailCheckRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("이메일 또는 인증 코드가 잘못되었습니다.");
        }

        if (!verificationService.verifyCode(emailCheckRequest.getEmail(), emailCheckRequest.getAuthCode())) {
            return ResponseEntity.badRequest().body("인증 코드가 일치하지 않습니다.");
        }

        return ResponseEntity.ok("인증이 완료되었습니다.");
    }
}