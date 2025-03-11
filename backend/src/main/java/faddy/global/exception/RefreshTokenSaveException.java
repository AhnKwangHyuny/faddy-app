package faddy.global.exception;

import lombok.Getter;

@Getter
public class RefreshTokenSaveException extends RuntimeException {

    private final int code;
    private final String message;

    public RefreshTokenSaveException(final ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

}