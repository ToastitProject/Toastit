package alcoholboot.toastit.global.config.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * @deprecated SSR 기반의 Controller 프로젝트이기에 해당 응답 코드는 아직 사용 미정입니다 :)
 */
@Getter
@AllArgsConstructor
public enum CommonResponseCode implements ResponseCode {
    SUCCESS(100, HttpStatus.OK, "요청이 성공됐습니다"),
    CREATED(101, HttpStatus.CREATED, "리소스가 성공적으로 생성되었습니다"),
    ACCEPTED(102, HttpStatus.ACCEPTED, "요청이 접수되었습니다"),
    NO_CONTENT(103, HttpStatus.NO_CONTENT, "요청이 성공적으로 처리되었으나 반환할 내용이 없습니다"),
    RESET_CONTENT(104, HttpStatus.RESET_CONTENT, "컨텐츠를 초기화하십시오"),
    PARTIAL_CONTENT(105, HttpStatus.PARTIAL_CONTENT, "부분적인 내용이 반환되었습니다");

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
