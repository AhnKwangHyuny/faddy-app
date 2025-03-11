package faddy.global.exception;

import lombok.Getter;

@Getter
public class InternalServerException extends RuntimeException {

    private String message;
    private int code;


    public InternalServerException(final ExceptionCode exCode) {
        this.message = exCode.getMessage();
        this.code = exCode.getCode();
    }

    public InternalServerException() {
        super();
    }

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerException(Throwable cause) {
        super(cause);
    }

    protected InternalServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
