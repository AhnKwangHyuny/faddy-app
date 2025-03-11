package faddy.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class ErrorApiResponse<T> extends ApiResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private ErrorApiResponse(int status, String errorMessage) {
        super(status, errorMessage);
    }

    private ErrorApiResponse(int status, String errorMessage, T data) {
        super(status, errorMessage);
        this.data = data;
    }

    public static <T> ResponseEntity<ErrorApiResponse<T>> of(HttpStatus status, String errorMessage) {
        return ResponseEntity.status(status).body(new ErrorApiResponse<>(status.value(), errorMessage));
    }

    public static <T> ResponseEntity<ErrorApiResponse<T>> of(int status, String errorMessage) {
        return ResponseEntity.status(status).body(new ErrorApiResponse<>(status, errorMessage));
    }

    public static <T> ResponseEntity<ErrorApiResponse<T>> of(HttpStatus status, String errorMessage, T data) {
        return ResponseEntity.status(status).body(new ErrorApiResponse<>(status.value(), errorMessage, data));
    }

    public static <T> ResponseEntity<ErrorApiResponse<T>> of(int status, String errorMessage, T data) {
        return ResponseEntity.status(status).body(new ErrorApiResponse<>(status, errorMessage, data));
    }
}
