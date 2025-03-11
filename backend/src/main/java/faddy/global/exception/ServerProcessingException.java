package faddy.global.exception;

import lombok.Getter;

@Getter
public class ServerProcessingException extends RuntimeException{

    private final int code;
    private final String message;

    public ServerProcessingException(final ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }


}
