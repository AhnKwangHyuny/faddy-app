package faddy.like.service.useCase;

import faddy.like.type.ContentType;

import java.util.List;

public interface LikeRedisService {

    /**
     *  객체 생성 시 like 초기화
     *
     * @param objectId    좋아요 대상 객체의 ID
     * @param contentType 좋아요 대상 객체의 타입 (Snap, StyleBoard, StyleBoardComment, User)
     */
    void initializeLikes(Long objectId, ContentType contentType);

    /**
     * 특정 객체에 대한 사용자의 좋아요 정보를 Redis에 저장
     *
     * @param objectId    좋아요 대상 객체의 ID
     * @param userId      좋아요를 누른 사용자의 ID
     * @param contentType 좋아요 대상 객체의 타입 (Snap, StyleBoard, StyleBoardComment, User)
     */
    void saveLike(Long objectId, Long userId, ContentType contentType);

    /**
     * 특정 객체에 대한 사용자의 좋아요 정보를 Redis에서 삭제
     *
     * @param objectId    좋아요 대상 객체의 ID
     * @param userId      좋아요를 취소한 사용자의 ID
     * @param contentType 좋아요 대상 객체의 타입 (Snap, StyleBoard, StyleBoardComment, User)
     */
    void removeLike(Long objectId, Long userId, ContentType contentType);

    /**
     * 특정 객체에 대한 사용자의 좋아요 여부를 확인
     *
     * @param objectId    좋아요 대상 객체의 ID
     * @param userId      좋아요 여부를 확인할 사용자의 ID
     * @param contentType 좋아요 대상 객체의 타입 (Snap, StyleBoard, StyleBoardComment, User)
     * @return 사용자가 해당 객체에 좋아요를 눌렀으면 true, 아니면 false
     */
    boolean isLiked(Long objectId, Long userId, ContentType contentType);

    /**
     * 특정 객체에 대한 전체 좋아요 수를 반환
     *
     * @param objectId    좋아요 수를 계산할 대상 객체의 ID
     * @param contentType 좋아요 대상 객체의 타입 (Snap, StyleBoard, StyleBoardComment, User)
     * @return 해당 객체에 대한 전체 좋아요 수
     */
    int countLikes(Long objectId, ContentType contentType);

    List<Long> getLikedUserIds(Long objectId, ContentType contentType);


    /**
     * 특정 객체에 대한 좋아요 정보를 Redis에서 삭제
     *
     * @param objectId    좋아요 대상 객체의 ID
     * @param contentType 좋아요 대상 객체의 타입 (Snap, StyleBoard, StyleBoardComment, User)
     */
    void deleteLikes(Long objectId, ContentType contentType);
}