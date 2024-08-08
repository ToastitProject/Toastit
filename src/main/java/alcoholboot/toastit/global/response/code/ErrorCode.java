package alcoholboot.toastit.global.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ResponseCode {
    TEST_ERROR(100, HttpStatus.BAD_REQUEST, "테스트 에러입니다."),

    TOKEN_EXPIRED(401, HttpStatus.UNAUTHORIZED, "Token has expired"),
    UNSUPPORTED_TOKEN(401, HttpStatus.BAD_REQUEST, "Unsupported token"),
    INVALID_TOKEN(401, HttpStatus.BAD_REQUEST, "Invalid token"),
    ILLEGAL_ARGUMENT(400, HttpStatus.BAD_REQUEST, "Illegal argument"),
    INTERNAL_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String data;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getData() {
        return data;
    }
}
