package faddy.api.Dto;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class ResponseDto<T> {

    private String responseCode;
    private String responseMessage;
    private T data;

    public ResponseDto(final String responseCode, final String responseMessage){
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.data = null;
    }


    public static <T> ResponseDto<T> response(final String responseCode, final String responseMessage){
        return response(responseCode, responseMessage, null);
    }

    public static <T> ResponseDto<T> response(final String responseCode, final String responseMessage, final T data){
        return ResponseDto.<T>builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .data(data)
                .build();
    }

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(String.valueOf(HttpStatus.OK.value()), "Success", data);
    }
}
