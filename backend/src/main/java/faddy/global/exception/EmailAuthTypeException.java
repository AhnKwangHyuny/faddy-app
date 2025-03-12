package faddy.global.exception;

import lombok.Getter;

@Getter
public class EmailAuthTypeException extends RuntimeException {

    private String message;
    private int code;


    public EmailAuthTypeException(final ExceptionCode exCode) {
        this.message = exCode.getMessage();
        this.code = exCode.getCode();
    }

    public EmailAuthTypeException() {
        super();
    }

    public EmailAuthTypeException(String message) {
        super(message);
    }

    public EmailAuthTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailAuthTypeException(Throwable cause) {
        super(cause);
    }

    protected EmailAuthTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
