package faddy.global.exception;

import lombok.Getter;

@Getter
public class AuthorizationException extends RuntimeException {

    private final int code;
    private final String message;

    public AuthorizationException(final ExceptionCode exceptionCode) {

        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();

    }

    public AuthorizationException(final int code , final String message) {

        this.code = code;
        this.message = message;
    }

}