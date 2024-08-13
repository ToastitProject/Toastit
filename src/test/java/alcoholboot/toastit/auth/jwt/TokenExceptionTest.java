package alcoholboot.toastit.auth.jwt;

import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;
import alcoholboot.toastit.global.config.response.exception.CustomException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TokenExceptionTest {

    @GetMapping("/expired-token")
    public void expiredToken() {
        throw new CustomException(CommonExceptionCode.JWT_EXPIRED_ERROR);
    }

    @GetMapping("/unsupported-token")
    public void unsupportedToken() {
        throw new CustomException(CommonExceptionCode.JWT_UNSUPPORTED_ERROR);
    }

    @GetMapping("/invalid-token")
    public void invalidToken() {
        throw new CustomException(CommonExceptionCode.JWT_INVALID_ERROR);
    }

    @GetMapping("/illegal-argument")
    public void illegalArgument() {
        throw new CustomException(CommonExceptionCode.BAD_REQUEST);
    }

    @GetMapping("/internal-error")
    public void internalError() {
        throw new CustomException(CommonExceptionCode.JWT_INTERNAL_ERROR);
    }
}

