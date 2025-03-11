package faddy.global.exception;

import lombok.Getter;

@Getter
public class ChatServiceException extends RuntimeException {

    private final int code;
    private final String message;

    public ChatServiceException(final ExceptionCode exceptionCode) {

        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();

    }

    public ChatServiceException(final int code , final String message) {

        this.code = code;
        this.message = message;
    }
}
