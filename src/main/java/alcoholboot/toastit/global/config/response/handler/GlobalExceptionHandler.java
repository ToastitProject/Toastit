package alcoholboot.toastit.global.config.response.handler;

import alcoholboot.toastit.global.config.response.exception.CustomException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException ex, Model model) {
        model.addAttribute("code", ex.getErrorCode().getCode());
        model.addAttribute("message", ex.getErrorCode().getData());
        return "error";
    }
}

