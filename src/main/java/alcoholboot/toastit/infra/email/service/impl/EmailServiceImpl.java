package alcoholboot.toastit.infra.email.service.impl;

import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;
import alcoholboot.toastit.global.config.response.exception.CustomException;
import alcoholboot.toastit.infra.email.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 이메일 발송 서비스 구현체.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    // JavaMailSender를 주입받아 이메일을 발송합니다.
    private final JavaMailSender mailSender;

    /**
     * 간단한 이메일 메시지를 전송합니다.
     *
     * @param to 수신자 이메일 주소
     * @param subject 이메일 제목
     * @param text 이메일 본문
     */
    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message); // 이메일 전송
    }

    /**
     * HTML 형식의 이메일을 전송합니다.
     *
     * @param to 수신자 이메일 주소
     * @param subject 이메일 제목
     * @param body 이메일 본문 (HTML 형식)
     */
    @Override
    public void sendFormMail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setSubject(subject);
            messageHelper.setTo(to);
            messageHelper.setFrom(fromEmail, "ToastIT");
            messageHelper.setText(body, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new CustomException(CommonExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 인증 코드가 포함된 HTML 이메일 폼을 반환합니다.
     *
     * @param authcode 인증 코드
     * @return 인증 코드가 포함된 이메일 본문
     */
    @Override
    public String setAuthForm(String authcode) {
        String templatePath = "/templates/auth/mail-form.html"; // 이메일 템플릿 경로
        String htmlContent = null;

        try (InputStream inputStream = getClass().getResourceAsStream(templatePath)) {
            if (inputStream == null) {
                throw new CustomException(CommonExceptionCode.INTERNAL_SERVER_ERROR);
            }
            htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CustomException(CommonExceptionCode.INTERNAL_SERVER_ERROR);
        }

        // {{authcode}}를 실제 인증 코드로 대체
        htmlContent = htmlContent.replace("{{authcode}}", authcode);

        return htmlContent;
    }
}