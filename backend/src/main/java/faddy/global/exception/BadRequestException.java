package faddy.global.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{

    private final int code;
    private final String message;

    public BadRequestException(final ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public BadRequestException(final int code , final String message) {
        this.code = code;
        this.message = message;
    }

}
