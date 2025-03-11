package faddy.global.exception;

public class FollowException extends RuntimeException{
    private final int code;
    private final String message;

    public FollowException(final ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public FollowException(final int code , final String message) {
        this.code = code;
        this.message = message;
    }

    public FollowException(final int code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

        public int getErrorCode() {
        return this.code;
    }

        public String getErrorMessage() {
        return this.message;
    }
}
