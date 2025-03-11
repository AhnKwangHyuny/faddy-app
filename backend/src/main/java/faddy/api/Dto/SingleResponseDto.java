package faddy.api.Dto;

public class SingleResponseDto<T> {
    private T body;

    public SingleResponseDto(T body) {
        this.body = body;
    }


    // body 필드에 대한 getter 메소드
    public T getbody() {
        return body;
    }
}
