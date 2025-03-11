package faddy.like.service;

import faddy.global.exception.DeleteEntityException;
import faddy.global.exception.SaveEntityException;
import faddy.like.domain.Like;
import faddy.like.repository.LikeCustomRepository;
import faddy.like.repository.LikeJpaRepository;
import faddy.like.service.useCase.LikeService;
import faddy.like.type.ContentType;
import faddy.snap.domain.Snap;
import faddy.snap.service.SnapService;
import faddy.styleBoard.domain.StyleBoard;
import faddy.styleBoard.service.useCase.StyleBoardDetailService;
import faddy.styleBoardComment.domain.StyleBoardComment;
import faddy.styleBoardComment.service.useCase.GetStyleBoardCommentService;
import faddy.user.domain.User;
import faddy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final LikeJpaRepository likeRepository;
    private final LikeCustomRepository likeCustomRepository;

    private final UserService userService;
    private final SnapService snapService;
    private final StyleBoardDetailService styleBoardDetailService;
    private final GetStyleBoardCommentService getStyleBoardCommentService;

    private static final String CREATE_ERROR_MESSAGE = "좋아요 엔티티 저장에 실패했습니다.";

    @Transactional(readOnly = true)
    @Override
    public String getLikeCount(Long snapId) {
        // Read Through 전략
        String key = "snap:" + snapId;
        Object likeData = redisTemplate.opsForHash().get(key, "likeCount");

        // 캐싱 실패 -> DB 조회
        if (likeData == null) {
            Snap snap = snapService.getActiveSnapById(snapId);
            String likeCount = String.valueOf(likeRepository.countBySnap(snap));

            // Redis에 좋아요 수 캐싱
            redisTemplate.opsForHash().put(key, "likeCount", likeCount);

            // Redis에서 Set을 사용하여 userIds 관리
            Set<String> userIds = likeRepository.findBySnap(snap).stream()
                    .map(like -> like.getUser().getId().toString())
                    .collect(Collectors.toSet());

            redisTemplate.opsForSet().add(key + ":userIds", userIds.toArray(new String[0]));

            return likeCount;
        }
        return likeData.toString();
    }


    @Override
    @Transactional
    public void like(Long userId, Long snapId) {
        // write through 전략
        String key = "snap:" + snapId;
        User user = userService.findUserById(userId);
        Snap snap = snapService.getActiveSnapById(snapId);

        // DB에 데이터 저장
        Like like = new Like(user, snap);

        // 연관관계 설정 (user <-> like, like -> snap)
        user.addLike(like);
        likeRepository.save(like);

        // Redis에 좋아요 수 저장
        // 'likeCount'를 1 증가시킴
        redisTemplate.opsForHash().increment(key, "likeCount", 1);

        // Redis에 사용자 ID 저장
        redisTemplate.opsForSet().add(key + ":userIds", String.valueOf(userId));


    }

    @Override
    @Transactional
    public void unlike(Long userId, Long snapId) {

        // Write Through 패턴
        String key = "snap:" + snapId;
        User user = userService.getUserById(userId);
        Snap snap = snapService.getSnapById(snapId);

        // db에서 데이터 삭제
        Like like = likeRepository.findByUserAndSnap(user, snap).orElseThrow();
        likeRepository.delete(like);

        // Redis에서 좋아요 수 감소
        redisTemplate.opsForHash().increment(key, "likeCount", -1);

        // Redis Set에서 사용자 ID 삭제
        redisTemplate.opsForSet().remove(key + ":userIds", String.valueOf(userId));

        // 모든 사용자가 좋아요를 취소한 경우, 좋아요 수 확인 후 Set과 Hash 키 삭제
        String likeCountStr = (String) redisTemplate.opsForHash().get(key, "likeCount");

        Long likeCount = Long.parseLong(likeCountStr);

        if (likeCount == null || likeCount <= 0) {

            redisTemplate.delete(Arrays.asList(key, key + ":userIds"));
        }
    }

    @Override
    public Set<String> getUserIds(Long snapId) {

        String key = "snap:" + snapId + ":userIds";

        Set<Object> userIds = redisTemplate.opsForSet().members(key);

        // userIds가 null이 아닌 경우 해당 값을 반환하고, 그렇지 않으면 빈 Set을 반환
        return userIds != null ? userIds.stream().map(Object::toString).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @Override
    public boolean hasLiked(Long userId, Long snapId) {

        String key = "snap:" + snapId + ":userIds";
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, String.valueOf(userId)));
    }

    @Override
    @Transactional
    public void createLike(String objectType, Long objectId, String token) {
        try {
            //유저 엔티티 조회
            User user = userService.findUserByToken(token);

            //object type to ContentType
            ContentType contentType = ContentType.fromString(objectType);

            //ContentType에 따라서 like 저장 로직 수행
            switch (contentType) {
                case STYLE_BOARD:
                    handleStyleBoardLike(user, objectId);
                    break;
                case SNAP:
                    handleSnapLike(user, objectId);
                    break;
                case STYLE_BOARD_COMMENT:
                    handleStyleBoardCommentLike(user, objectId);
                    break;
                default:
                    throw new SaveEntityException(HttpStatus.BAD_REQUEST, "정해지지 않은 contentType 입니다. [좋아요 생성 실패]");
            }

        } catch (Exception e) {
            throw new SaveEntityException(HttpStatus.BAD_REQUEST , CREATE_ERROR_MESSAGE);
        }
    }


    @Override
    @Transactional
    public void deleteLike(ContentType type, Long objectId) {
        // ContentType에 따라서 like 삭제 로직 수행
        switch (type) {
            case STYLE_BOARD:
                handleDeleteStyleBoardLike(objectId);
                break;
            case SNAP:
                handleDeleteSnapLike(objectId);
                break;
            case STYLE_BOARD_COMMENT:
                handleDeleteStyleBoardCommentLike(objectId);
                break;
            default:
                log.warn("정해지지 않은 contentType 입니다. [좋아요 삭제 실패]");
                throw new DeleteEntityException(HttpStatus.BAD_REQUEST, "정해지지 않은 contentType 입니다. [좋아요 삭제 실패]" , objectId);
        }
    }

    @Transactional
    private void handleDeleteStyleBoardLike(Long objectId) {
        StyleBoard styleBoard = styleBoardDetailService.getStyleBoard(objectId);
        List<Like> likes = likeRepository.findByStyleBoard(styleBoard);

        try {
            likeRepository.deleteAll(likes);
        } catch (Exception e) {
            throw new DeleteEntityException(HttpStatus.BAD_REQUEST, "스타일보드 좋아요 삭제에 실패했습니다." , objectId);
        }
    }

    @Transactional
    private void handleDeleteSnapLike(Long objectId) {
        Snap snap = snapService.getSnapById(objectId);
        List<Like> likes = likeRepository.findBySnap(snap);

        try {
            likeRepository.deleteAll(likes);
        } catch (Exception e) {
            throw new DeleteEntityException(HttpStatus.BAD_REQUEST, "스냅 좋아요 삭제에 실패했습니다." , objectId);
        }
    }

    @Transactional
    private void handleDeleteStyleBoardCommentLike(Long objectId) {
        StyleBoardComment comment = getStyleBoardCommentService.loadStyleBoardById(objectId);
        List<Like> likes = likeRepository.findByStyleBoardComment(comment);

        try {
            likeRepository.deleteAll(likes);
        } catch (Exception e) {
            throw new DeleteEntityException(HttpStatus.BAD_REQUEST, "스타일보드 댓글 삭제에 실패했습니다" , objectId);
        }
    }


    @Transactional
    private void handleStyleBoardLike(User user, Long objectId) {

        StyleBoard styleBoard = styleBoardDetailService.getStyleBoard(objectId);
        Like like = Like.createLikeForStyleBoard(user, styleBoard);

        try {
            // like 저장
           likeRepository.save(like);

        } catch (Exception e) {
            throw new SaveEntityException(HttpStatus.BAD_REQUEST, CREATE_ERROR_MESSAGE);
        }
    }

    @Transactional
    private void handleSnapLike(User user, Long objectId) {

        Snap snap = snapService.getSnapById(objectId);
        Like like = Like.createLikeForSnap(user, snap);

        try {
            // like 저장
            likeRepository.save(like);

        } catch (Exception e) {
            throw new SaveEntityException(HttpStatus.BAD_REQUEST, CREATE_ERROR_MESSAGE);
        }
    }

    @Transactional
    private void handleStyleBoardCommentLike(User user, Long objectId) {

        StyleBoardComment comment = getStyleBoardCommentService.loadStyleBoardById(objectId);
        Like like = Like.createLikeForStyleBoardComment(user, comment);

        try {
            // like 저장
            likeRepository.save(like);

        } catch (Exception e) {
            throw new SaveEntityException(HttpStatus.BAD_REQUEST, CREATE_ERROR_MESSAGE);
        }
    }



    @Override
    @Transactional
    public void batchSaveStyleBoardCommentLikes(StyleBoardComment comment,List<User> users) {

        // Comment content type
        ContentType contentType = ContentType.STYLE_BOARD_COMMENT;

        try {
            // like entity 생성
            List<Like> likes = users.stream()
                    .map(user -> Like.createLikeForStyleBoardComment(user, comment))
                    .collect(Collectors.toList());

            // like 저장 (jdbc batch save)
            likeCustomRepository.batchSaveLikesForStyleBoardComments(likes);

        } catch (Exception e) {
            throw new SaveEntityException(HttpStatus.BAD_REQUEST, CREATE_ERROR_MESSAGE);
        }

    }

    @Override
    @Transactional
    public void batchSaveStyleBoardLikes(StyleBoard styleBoard, List<User> users) {
        // StyleBoard content type
        ContentType contentType = ContentType.STYLE_BOARD;

        try {
            // like entity 생성
            List<Like> likes = users.stream()
                    .map(user -> Like.createLikeForStyleBoard(user, styleBoard))
                    .collect(Collectors.toList());

            // like 저장 (jdbc batch save)
            likeCustomRepository.batchSaveLikesForStyleBoards(likes);

        } catch (Exception e) {
            e.printStackTrace();
            throw new SaveEntityException(HttpStatus.BAD_REQUEST, CREATE_ERROR_MESSAGE);
        }
    }
}