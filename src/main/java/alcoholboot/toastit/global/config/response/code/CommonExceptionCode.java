package alcoholboot.toastit.global.config.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonExceptionCode implements ResponseCode {
    /**
     * File error
     */
    FILE_ERROR(400, HttpStatus.BAD_REQUEST, "파일 에러"),

    /**
     *  Image Error
     */
    // 파일 전역 IO Error 처리
    IMAGE_ERROR(400, HttpStatus.BAD_REQUEST, "이미지 업로드 에러"),

    // 파일 못찾는 경우 ex) null
    IMAGE_NOT_FOUND(400, HttpStatus.BAD_REQUEST, "이미지를 찾을 수 없습니다."),

    // 파일 형식을 지키지 않은 경우
    IMAGE_FORMAT_ERROR(400, HttpStatus.BAD_REQUEST, "이미지 형식이 올바르지 않습니다."),

    // 이미지 리사이즈 실패
    IMAGE_RESIZE_ERROR(400, HttpStatus.BAD_REQUEST, "이미지 업로드 에러"),

    // 임시 이미지 x
    IMAGE_NOT_TEMP(400, HttpStatus.BAD_REQUEST, "이미지 에러"),

    /**
     * Token Error
     */
    // 토큰 만료
    JWT_EXPIRED_ERROR(401, HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    // 잘못된 형식
    JWT_INVALID_ERROR(401, HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    // 토큰 미존재
    JWT_UNKNOWN_ERROR(401, HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    // 변조된 토큰
    JWT_UNSUPPORTED_ERROR(401, HttpStatus.UNAUTHORIZED, "변조된 토큰입니다."),
    // 알 수 없는 오류
    JWT_INTERNAL_ERROR(401, HttpStatus.UNAUTHORIZED, "알 수 없는 오류가 발생했습니다."),
    // JWT 해독 오류 (토큰의 내용이 손상되었거나 읽을 수 없음)
    JWT_DECRYPTION_ERROR(401, HttpStatus.UNAUTHORIZED, "토큰을 해독할 수 없습니다."),

    /**
     * LocationCocktail Error
     */
    LOCATION_COCKTAIL_NOT_FOUND(404, HttpStatus.NOT_FOUND, "현재 위치에 대해 칵테일을 추천할 수 없습니다"),

    /**
     * User Error
     */
    // 사용 중인 이메일
    EXIST_EMAIL_ERROR(400, HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일이며, 등록된 계정이 있습니다. 비밀번호를 잊으셨다면, 비밀번호 찾기를 이용해주세요."),
    // 올바르지 않은 요청값
    FILED_ERROR(400, HttpStatus.BAD_REQUEST, "요청값이 올바르지 않습니다."),
    // 이메일 인증번호 불일치
    NOT_MATCH_AUTH_CODE(400, HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),
    // 이메일 비밀번호 불일치
    NOT_MATCH_EMAILL_OR_PASSWORD(400, HttpStatus.BAD_REQUEST, "이메일 혹은 비밀번호가 일치하지 않습니다."),
    // 유저를 찾을 수 없음
    NOT_FOUND_USER(400, HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),

    /**
     * 4** client
     */
    // 400
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 매개 변수가 포함됐습니다."),
    // 401
    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    // 403
    FORBIDDEN(403, HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    // 404
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다"),
    // 408
    REQUEST_TIMEOUT(408, HttpStatus.REQUEST_TIMEOUT, "요청 시간이 초과되었습니다."),
    // 415
    UNSUPPORTED_MEDIA_TYPE(415, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원되지 않는 미디어 타입입니다."),
    // 401
    TIMEOUT_LOGOUT(401, HttpStatus.UNAUTHORIZED, "일정 시간 동안 활동이 없어 로그아웃되었습니다. 계속하려면 다시 로그인해주세요."),
    // 403
    MEMBER_SIGNUP_REQUIRED(403, HttpStatus.FORBIDDEN, "아직 회원이 아니신가요? 회원가입 후 이용해주세요."),

    /**
     * 5** Server Error
     */
    // 500
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    // 502
    BAD_GATEWAY(502, HttpStatus.BAD_GATEWAY, "잘못된 게이트웨이입니다."),
    // 503
    SERVICE_UNAVAILABLE(503, HttpStatus.SERVICE_UNAVAILABLE, "서비스를 사용할 수 없습니다."),
    // 504
    GATEWAY_TIMEOUT(504, HttpStatus.GATEWAY_TIMEOUT, "게이트웨이 시간 초과입니다."),
    // 505
    HTTP_VERSION_NOT_SUPPORTED(505, HttpStatus.HTTP_VERSION_NOT_SUPPORTED, "HTTP 버전을 지원하지 않습니다."),
    // 507
    INSUFFICIENT_STORAGE(507, HttpStatus.INSUFFICIENT_STORAGE, "저장 공간이 부족합니다."),
    // 511
    NETWORK_AUTHENTICATION_REQUIRED(511, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "네트워크 인증이 필요합니다.");

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