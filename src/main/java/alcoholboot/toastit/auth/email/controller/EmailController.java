package alcoholboot.toastit.auth.email.controller;

import alcoholboot.toastit.feature.user.service.UserManagementService;
import alcoholboot.toastit.auth.email.controller.request.EmailCheckRequest;
import alcoholboot.toastit.auth.email.controller.request.EmailSendRequest;
import alcoholboot.toastit.auth.email.service.EmailService;
import alcoholboot.toastit.auth.email.service.VerificationService;
import alcoholboot.toastit.auth.email.util.RandomAuthCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final UserManagementService userManagementService;
    private final VerificationService verificationService;
    private final EmailService emailService;

    /**
     * 인증번호 발송 메소드
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendAuthEmail(@RequestBody @Valid EmailSendRequest emailSendRequest, BindingResult bindingResult) {

        log.debug(emailSendRequest.getEmail() + " 해당 이메일이 요청되었습니다.");

        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            log.error("잘못된 이메일 형식이 전달되었습니다.");
            return ResponseEntity.badRequest().body("이메일 형식이 잘못되었습니다.");
        }

        // 사용자가 존재하는지 확인
        if (userManagementService.existsByEmail(emailSendRequest.getEmail())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 이메일이며, 등록된 계정이 있습니다. 비밀번호 찾기를 이용해주세요.");
        }

        // 랜덤 인증 코드 생성
        String authCode = RandomAuthCode.generate();

        // redis에 인증 코드 저장
        verificationService.saveCode(emailSendRequest.getEmail(), authCode);

        log.debug("발급된 인증번호는 " + authCode + "입니다.");

        // 메일 제목
        String subject = "[ToastIT] 이메일 인증번호 : " + authCode;

        // 메일 본문
        String body = emailService.setAuthForm(authCode);

        // 메일 발송
        emailService.sendFormMail(emailSendRequest.getEmail(), subject, body);

        log.debug(emailSendRequest.getEmail() + " 해당 이메일로 인증코드가 발송되었습니다.");
        return ResponseEntity.ok("인증 메일이 발송되었습니다.");
    }

    /**
     * 인증번호 검증 메소드
     */
    @PostMapping("/verify")
    public ResponseEntity<String> checkAuthEmail(@RequestBody @Valid EmailCheckRequest emailCheckRequest, BindingResult bindingResult) {

        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("이메일 또는 인증 코드가 잘못되었습니다.");
        }

        // redis에 저장된 인증번호와 비교하여 확인
        if (!verificationService.verifyCode(emailCheckRequest.getEmail(), emailCheckRequest.getAuthCode())) {
            return ResponseEntity.badRequest().body("인증 코드가 일치하지 않습니다.");
        }

        return ResponseEntity.ok("인증이 완료되었습니다.");
    }
}