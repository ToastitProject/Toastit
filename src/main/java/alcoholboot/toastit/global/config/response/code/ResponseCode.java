package alcoholboot.toastit.global.config.response.code;

import org.springframework.http.HttpStatus;

public interface ResponseCode {
    Integer getCode();
    HttpStatus getHttpStatus();
    String getData();
}
