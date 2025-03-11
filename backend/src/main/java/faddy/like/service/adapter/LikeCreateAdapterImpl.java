package faddy.like.service.adapter;

import faddy.global.exception.BadRequestException;
import faddy.global.exception.SaveEntityException;
import faddy.like.domain.Like;
import faddy.like.repository.LikeJpaRepository;
import faddy.like.service.adapter.useCase.LikeCreateAdapter;
import faddy.like.type.ContentType;
import faddy.snap.domain.Snap;
import faddy.styleBoard.domain.StyleBoard;
import faddy.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeCreateAdapterImpl implements LikeCreateAdapter {

    private final LikeJpaRepository likeRepository;

    //error message
    private static final String CREATE_ERROR_MESSAGE = "좋아요 생성에 실패했습니다.";
    private static final String UNKNOWN_ERROR_MESSAGE = "정해지지 않은 contentType 입니다. [좋아요 생성 실패] ";

    @Override
    @Transactional
    public void create(User user, Object content, ContentType type) {
        try {

            Like like = new Like();

            switch (type) {
                case STYLE_BOARD:
                    content = (StyleBoard) content;
                    break;
                case SNAP:
                    content = (Snap) content;
                    break;
                case STYLE_BOARD_COMMENT:
                    content = (StyleBoard) content;
                    break;
                default:
                    throw new BadRequestException( HttpStatus.BAD_REQUEST.value() , UNKNOWN_ERROR_MESSAGE + type);
            }

            likeRepository.save(like);

        } catch (Exception e) {
            throw new SaveEntityException(HttpStatus.INTERNAL_SERVER_ERROR, CREATE_ERROR_MESSAGE);
        }
    }
}
