package alcoholboot.toastit.global.response.exception;

import alcoholboot.toastit.global.response.code.CommonExceptionCode;

public class CustomException extends RuntimeException {
    private final CommonExceptionCode commonExceptionCode;

    public CustomException(CommonExceptionCode commonExceptionCode) {
        super(commonExceptionCode.getData());
        this.commonExceptionCode = commonExceptionCode;
    }

    public CommonExceptionCode getErrorCode() {
        return commonExceptionCode;
    }
}


