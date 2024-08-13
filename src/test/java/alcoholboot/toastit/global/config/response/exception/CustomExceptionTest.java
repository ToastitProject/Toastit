package alcoholboot.toastit.global.config.response.custom;

import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;
import alcoholboot.toastit.global.config.response.exception.CustomException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class CustomExceptionTest {

    @GetMapping("/TIMEOUT_LOGOUT")
    public void timeoutLogout() {
        throw new CustomException(CommonExceptionCode.TIMEOUT_LOGOUT);
    }

    @GetMapping("/MEMBER_SIGNUP_REQUIRED")
    public void memberSignupRequired() {
        throw new CustomException(CommonExceptionCode.MEMBER_SIGNUP_REQUIRED);
    }
}