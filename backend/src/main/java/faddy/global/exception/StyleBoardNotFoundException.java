package faddy.global.exception;

public class StyleBoardNotFoundException extends RuntimeException {
    public StyleBoardNotFoundException(Long styleBoardId) {
        super(String.format("Style board with ID %d not found.", styleBoardId));
    }
}