package alcoholboot.toastit.global.response.code;

import org.springframework.http.HttpStatus;

public interface ResponseCode {
    Integer getCode();
    HttpStatus getHttpStatus();
    String getData();
}
