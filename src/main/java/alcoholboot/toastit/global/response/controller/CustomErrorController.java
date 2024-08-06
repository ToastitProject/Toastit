package alcoholboot.toastit.global.response.controller;

import alcoholboot.toastit.global.response.dto.ErrorCode;
import jakarta.servlet.RequestDispatcher;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // 기본값

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            httpStatus = HttpStatus.resolve(statusCode);
        }

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR; // 기본값
        for (ErrorCode code : ErrorCode.values()) {
            if (code.getHttpStatus().equals(httpStatus)) {
                errorCode = code;
                break;
            }
        }

        String message = errorCode.getMessage(); // 메시지 설정

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code", errorCode.getCode());
        modelAndView.addObject("message", message);

        return modelAndView;
    }
}

