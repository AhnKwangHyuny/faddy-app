package faddy.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DeleteEntityException extends RuntimeException {

    private final String message;
    private final int code;
    private final Long entityId;  // 삭제 실패한 엔티티 ID

    public DeleteEntityException(ExceptionCode exceptionCode, Long entityId) {
        super(exceptionCode.getMessage());
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
        this.entityId = entityId;
    }

    public DeleteEntityException(final int code, final String message, Long entityId) {
        super(message);
        this.code = code;
        this.message = message;
        this.entityId = entityId;
    }

    public DeleteEntityException(HttpStatus httpStatus, final String message, Long entityId) {
        super(message);
        this.code = httpStatus.value();
        this.message = message;
        this.entityId = entityId;
    }

    public DeleteEntityException(final int code, final String message, final Throwable cause, Long entityId) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.entityId = entityId;
    }
}
