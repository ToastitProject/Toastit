package alcoholboot.toastit.feature.user.exception;

import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;

public class EmailVerificationException extends RuntimeException {
    public EmailVerificationException(CommonExceptionCode commonExceptionCode) {
        super(commonExceptionCode.getData());
    }
}