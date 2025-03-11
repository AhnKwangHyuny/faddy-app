package faddy.views.service.useCase;


import faddy.views.type.ContentType;
import org.springframework.transaction.annotation.Transactional;

public interface ViewRedisService {

    /**
     * 객체 생성 시 view 초기화
     *
     * @param objectId    조회수 대상 객체의 ID
     * @param contentType 조회수 대상 객체의 타입 (Snap, StyleBoard, StyleBoardComment, User)
     */
    void initializeViews(Long objectId, ContentType contentType);

    /**
     * 특정 객체에 대한 조회수 증가 정보를 Redis에 저장
     *
     * @param objectId    조회수 대상 객체의 ID
     * @param userId      조회한 사용자의 ID
     * @param contentType 조회수 대상 객체의 타입 (Snap, StyleBoard, StyleBoardComment, User)
     */
    void saveView(Long objectId, Long userId, ContentType contentType);

    /**
     * 특정 객체에 대한 사용자의 조회 여부를 확인
     *
     * @param objectId    조회수 대상 객체의 ID
     * @param userId      조회 여부를 확인할 사용자의 ID
     * @param contentType 조회수 대상 객체의 타입 (Snap, StyleBoard, StyleBoardComment, User)
     * @return 사용자가 해당 객체를 조회했으면 true, 아니면 false
     */
    boolean isViewed(Long objectId, Long userId, ContentType contentType);

    /**
     * 특정 객체에 대한 전체 조회수 반환
     *
     * @param objectId    조회수를 계산할 대상 객체의 ID
     * @param contentType 조회수 대상 객체의 타입 (Snap, StyleBoard, StyleBoardComment, User)
     * @return 해당 객체에 대한 전체 조회수
     */
    int countViews(Long objectId, ContentType contentType);

    /**
     * 특정 객체에 대한 조회수 정보를 Redis에서 삭제
     *
     * @param objectId    조회수 대상 객체의 ID
     * @param contentType 조회수 대상 객체의 타입 (Snap, StyleBoard, StyleBoardComment, User)
     */
    void deleteViews(Long objectId, ContentType contentType);

}
