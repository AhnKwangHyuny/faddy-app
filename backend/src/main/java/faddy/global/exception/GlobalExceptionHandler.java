package faddy.global.exception;

import faddy.global.response.ApiResponse;
import faddy.global.response.ErrorApiResponse;
import faddy.log.exception.ExceptionLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<? extends ApiResponse> handleBadRequestException(final BadRequestException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<? extends ApiResponse> handleNullPointerException(final NullPointerException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<? extends ApiResponse> handleAllExceptions(Exception e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<? extends ApiResponse> handleInternalServerException(final InternalServerException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<? extends ApiResponse> handleAuthenticationException(final AuthenticationException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(UnAuthorizationException.class)
    public ResponseEntity<? extends ApiResponse> handleUnAuthorizationException(final UnAuthorizationException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<? extends ApiResponse> handleIllegalArgumentException(final IllegalArgumentException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<? extends ApiResponse> handleJwtValidationException(final JwtValidationException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(RefreshTokenSaveException.class)
    public ResponseEntity<? extends ApiResponse> handleRefreshTokenSaveException(final RefreshTokenSaveException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(SaveEntityException.class)
    public ResponseEntity<? extends ApiResponse> handleSaveEntityException(final SaveEntityException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(SnapException.class)
    public ResponseEntity<? extends ApiResponse> handleSnapException(final SnapException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(FollowException.class)
    public ResponseEntity<? extends ApiResponse> handleFollowException(final FollowException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ChatRoomException.class)
    public ResponseEntity<? extends ApiResponse> handleChatRoomException(final ChatRoomException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(StyleBoardDataAccessException.class)
    public ResponseEntity<? extends ApiResponse> handleStyleBoardDataAccessException(final StyleBoardDataAccessException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(StyleBoardNotFoundException.class)
    public ResponseEntity<? extends ApiResponse> handleStyleBoardNotFoundException(final StyleBoardNotFoundException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<? extends ApiResponse> handleCommentNotFoundException(final CommentNotFoundException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(DbSyncException.class)
    public ResponseEntity<? extends ApiResponse> handleDbAsyncException(final DbSyncException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(DeleteEntityException.class)
    public ResponseEntity<? extends ApiResponse> handleDeleteEntityException(final DeleteEntityException e) {
        ExceptionLogger.logException(e);
        return ErrorApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

}
