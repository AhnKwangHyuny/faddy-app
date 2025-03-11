package faddy.like.service.adapter.useCase;

import faddy.like.type.ContentType;
import faddy.user.domain.User;
import org.springframework.transaction.annotation.Transactional;

public interface LikeCreateAdapter {

    /**
     *  최초 좋아용 엔티티 생성
     *  @param user 좋아요를 누른 유저
     *  @param content 좋아요를 누를 수 있는 객체 (초기 생성) - snap, styleBoard, comment, user
     *  @param type 좋아요를 누를 수 있는 객체 타입
     *
     * */
    void create (User user, Object content , ContentType type);


}
