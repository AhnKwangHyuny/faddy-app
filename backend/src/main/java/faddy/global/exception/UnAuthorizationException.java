package faddy.global.exception;

import lombok.Getter;

@Getter
public class UnAuthorizationException extends RuntimeException {

    private final int code;
    private final String message;

    public UnAuthorizationException(final ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
