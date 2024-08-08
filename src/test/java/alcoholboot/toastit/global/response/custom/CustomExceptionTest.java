package alcoholboot.toastit.global.response.custom;

import alcoholboot.toastit.global.response.code.CommonExceptionCode;
import alcoholboot.toastit.global.response.exception.CustomException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class CustomExceptionTest {

    @GetMapping("/error-test")
    public void testError() {
        throw new CustomException(CommonExceptionCode.TEST_ERROR);
    }

    @GetMapping("/expired-token")
    public void expiredToken() {
        throw new CustomException(CommonExceptionCode.TOKEN_EXPIRED);
    }

    @GetMapping("/unsupported-token")
    public void unsupportedToken() {
        throw new CustomException(CommonExceptionCode.UNSUPPORTED_TOKEN);
    }

    @GetMapping("/invalid-token")
    public void invalidToken() {
        throw new CustomException(CommonExceptionCode.INVALID_TOKEN);
    }

    @GetMapping("/illegal-argument")
    public void illegalArgument() {
        throw new CustomException(CommonExceptionCode.ILLEGAL_ARGUMENT);
    }

    @GetMapping("/internal-error")
    public void internalError() {
        throw new CustomException(CommonExceptionCode.INTERNAL_ERROR);
    }

    @GetMapping("main")
    public String testMain() {
        return "main";
    }
}
