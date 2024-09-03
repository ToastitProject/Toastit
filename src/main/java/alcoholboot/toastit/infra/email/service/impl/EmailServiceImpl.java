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

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    // JavaMailSender를 주입받아 이메일을 보낼 수 있도록 설정
    private final JavaMailSender mailSender;

    /**
     * 간단한 이메일 메시지를 보내는 메소드
     *
     * @param to 수신자 이메일 주소
     * @param subject 이메일 제목
     * @param text 이메일 본문
     */
    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        // SimpleMailMessage 객체 생성 및 설정
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to); // 수신자 설정
        message.setSubject(subject); // 제목 설정
        message.setText(text); // 본문 설정
        // 이메일 전송
        mailSender.send(message);
    }

    /**
     * html 폼 메일을 발송하는 메소드
     *
     * @param to      수신자 이메일 주소
     * @param subject 이메일 제목
     * @param body    이메일 본문(html)
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
     * 인증메일 폼에 인증코드를 추가하여 반환하는 메소드
     *
     * @param authcode 인증코드
     * @return 인증코드를 추가한 인증메일 폼
     */
    @Override
    public String setAuthForm(String authcode) {
        // 인증메일 폼 경로 설정
        String templatePath = "/templates/auth/mail-form.html";

        // 인증메일 폼 읽어오기
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
