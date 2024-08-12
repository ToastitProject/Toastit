package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.user.controller.request.EmailCheckRequest;
import alcoholboot.toastit.feature.user.controller.request.EmailSendRequest;
import alcoholboot.toastit.feature.user.service.EmailService;
import alcoholboot.toastit.feature.user.service.VerificationService;
import alcoholboot.toastit.feature.user.util.RandomAuthCode;
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
    private final VerificationService verificationService;
    private final EmailService emailService;

    /**
     * 인증번호 발송 메소드
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendAuthEmail(@RequestBody @Valid EmailSendRequest emailSendDto, BindingResult bindingResult) {

        log.info("이메일 전달 받음");

        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            log.info("이메일 에러 발생");
            return ResponseEntity.badRequest().body("이메일 형식이 잘못되었습니다.");
        }

        // 랜덤 인증 코드 생성
        String authCode = RandomAuthCode.generate();

        // redis에 인증 코드 저장
        verificationService.saveCode(emailSendDto.getEmail(), authCode);

        log.info(authCode);

        // 메일 발송
        emailService.sendSimpleMessage(emailSendDto.getEmail(), "[술프링부트] 이메일 인증번호 발송드립니다.", authCode);

        log.info("이메일 발송");
        return ResponseEntity.ok("인증 메일이 발송되었습니다.");
    }

    /**
     * 인증번호 검증 메소드
     */
    @PostMapping("/verify")
    public ResponseEntity<String> checkAuthEmail(@RequestBody @Valid EmailCheckRequest emailCheckDto, BindingResult bindingResult) {

        // 필드 에러 확인
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("이메일 또는 인증 코드가 잘못되었습니다.");
        }

        // redis에 저장된 인증번호와 비교하여 확인
        if (!verificationService.verifyCode(emailCheckDto.getEmail(), emailCheckDto.getAuthCode())) {
            return ResponseEntity.badRequest().body("인증 코드가 일치하지 않습니다.");
        }

        return ResponseEntity.ok("인증이 완료되었습니다.");
    }
}