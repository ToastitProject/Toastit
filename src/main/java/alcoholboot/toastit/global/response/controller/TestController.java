package alcoholboot.toastit.global.response.controller;

import alcoholboot.toastit.global.response.code.ErrorCode;
import alcoholboot.toastit.global.response.exception.CustomException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/error-test")
    public void testError() {
        throw new CustomException(ErrorCode.TEST_ERROR);
    }

    @GetMapping("/expired-token")
    public void expiredToken() {
        throw new CustomException(ErrorCode.TOKEN_EXPIRED);
    }

    @GetMapping("/unsupported-token")
    public void unsupportedToken() {
        throw new CustomException(ErrorCode.UNSUPPORTED_TOKEN);
    }

    @GetMapping("/invalid-token")
    public void invalidToken() {
        throw new CustomException(ErrorCode.INVALID_TOKEN);
    }

    @GetMapping("/illegal-argument")
    public void illegalArgument() {
        throw new CustomException(ErrorCode.ILLEGAL_ARGUMENT);
    }

    @GetMapping("/internal-error")
    public void internalError() {
        throw new CustomException(ErrorCode.INTERNAL_ERROR);
    }

    @GetMapping("main")
    public String testMain() {
        return "main";
    }
}
