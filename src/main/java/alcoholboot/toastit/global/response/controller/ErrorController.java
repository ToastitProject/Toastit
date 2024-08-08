package alcoholboot.toastit.global.response.controller;

import alcoholboot.toastit.global.response.exception.CustomException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException ex, Model model) {
        model.addAttribute("code", ex.getErrorCode().getCode());
        model.addAttribute("message", ex.getErrorCode().getData());
        return "error";
    }
}

