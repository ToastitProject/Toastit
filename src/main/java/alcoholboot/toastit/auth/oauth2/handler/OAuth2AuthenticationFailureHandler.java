package alcoholboot.toastit.auth.oauth2.handler;

import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;
import alcoholboot.toastit.global.config.response.exception.CustomException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("OAuth2 인증 실패: {}", exception.getMessage());

        // CustomException을 발생시켜 GlobalExceptionHandler로 처리하도록 함
        throw new CustomException(CommonExceptionCode.FILED_ERROR);
    }
}