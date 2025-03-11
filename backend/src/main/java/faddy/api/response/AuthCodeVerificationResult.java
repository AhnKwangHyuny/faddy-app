package faddy.api.response;

public class AuthCodeVerificationResult {

    private final boolean result;

    public  AuthCodeVerificationResult(boolean result) {
        this.result = result;
    }


    // result 필드에 대한 getter 메소드
    public boolean getResult() {
        return result;
    }
}
