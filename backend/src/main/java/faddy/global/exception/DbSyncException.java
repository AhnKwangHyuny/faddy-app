package faddy.global.exception;

import org.springframework.http.HttpStatus;

public class DbSyncException extends RuntimeException {
    private String message;
    private int code;

    public DbSyncException(ExceptionCode exceptionCode) {
        this.message = exceptionCode.getMessage();
        this.code = exceptionCode.getCode();
    }


    public DbSyncException(final int code , final String message) {
        this.code = code;
        this.message = message;
    }

    public DbSyncException(HttpStatus httpStatus , final String message) {
        this.code = httpStatus.value();
        this.message = message;
    }

    public DbSyncException(final int code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}
