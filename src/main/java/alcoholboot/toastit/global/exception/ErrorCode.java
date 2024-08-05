package alcoholboot.toastit.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Test Error
    TEST_ERROR(100, HttpStatus.BAD_REQUEST, "테스트 에러입니다."),

    // JWT Errors
    TOKEN_EXPIRED(401, HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    UNSUPPORTED_TOKEN(401, HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰입니다."),
    INVALID_TOKEN(401, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    ILLEGAL_ARGUMENT(400, HttpStatus.BAD_REQUEST, "잘못된 인수입니다."),

    NOT_FOUND_END_POINT(404, HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),

    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
