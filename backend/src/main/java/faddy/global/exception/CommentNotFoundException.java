package faddy.global.exception;

import lombok.Getter;

@Getter
public class CommentNotFoundException extends RuntimeException {

    private String message;
    private int code;

    public CommentNotFoundException(ExceptionCode exceptionCode) {
        this.message = exceptionCode.getMessage();
        this.code = exceptionCode.getCode();
    }


    public CommentNotFoundException(final int code , final String message) {
        this.code = code;
        this.message = message;
    }

    public CommentNotFoundException(final int code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

}
