package alcoholboot.toastit.global.response.exception;

import alcoholboot.toastit.global.response.dto.ApiResponse;
import alcoholboot.toastit.global.response.dto.ErrorCode;
import alcoholboot.toastit.global.response.exception.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler { //전역적인 예외처리를 담당

    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ApiResponse<?> handleNoPageFoundException(Exception e) {
        log.error("GlobalExceptionHandler catch NoHandlerFoundException : {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.NOT_FOUND_END_POINT));
    }

    @ExceptionHandler(value = {CustomException.class})
    public ApiResponse<?> handleCustomException(CustomException e) {
        log.error("handleCustomException() in GlobalExceptionHandler throw CustomException : {}", e.getMessage());
        return ApiResponse.fail(e);
    }

    @ExceptionHandler(value = {Exception.class})
    public ApiResponse<?> handleException(Exception e) {
        log.error("handleException() in GlobalExceptionHandler throw Exception : {}", e.getMessage());
        e.printStackTrace();
        return ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(value = {ExpiredJwtException.class})
    public ApiResponse<?> handleExpiredJwtException(ExpiredJwtException e) {
        log.error("handleExpiredJwtException() in GlobalExceptionHandler : {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.TOKEN_EXPIRED));
    }

    @ExceptionHandler(value = {UnsupportedJwtException.class})
    public ApiResponse<?> handleUnsupportedJwtException(UnsupportedJwtException e) {
        log.error("handleUnsupportedJwtException() in GlobalExceptionHandler : {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.UNSUPPORTED_TOKEN));
    }

    @ExceptionHandler(value = {MalformedJwtException.class})
    public ApiResponse<?> handleMalformedJwtException(MalformedJwtException e) {
        log.error("handleMalformedJwtException() in GlobalExceptionHandler : {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.INVALID_TOKEN));
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ApiResponse<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("handleIllegalArgumentException() in GlobalExceptionHandler : {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.ILLEGAL_ARGUMENT));
    }
}
