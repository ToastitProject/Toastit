package alcoholboot.toastit.infra.oauth2.handler;

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

/**
 * OAuth2 인증 실패 시 처리하는 핸들러.
 * 인증 실패 시 {@link CustomException}을 발생시켜 전역 예외 처리로 처리됩니다.
 */
@Slf4j
@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    /**
     * OAuth2 인증 실패 처리 메서드.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param exception 발생한 인증 예외
     * @throws IOException 입출력 예외 처리
     * @throws ServletException 서블릿 예외 처리
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("OAuth2 인증 실패: {}", exception.getMessage());

        // CustomException을 발생시켜 전역 예외 처리로 이동
        throw new CustomException(CommonExceptionCode.FILED_ERROR);
    }
}