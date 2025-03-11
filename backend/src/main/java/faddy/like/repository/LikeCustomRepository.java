package faddy.like.repository;

import faddy.like.domain.Like;

import java.util.List;

public interface LikeCustomRepository {

    List<Long> batchSaveLikesForSnaps(List<Like> likes);
    List<Long> batchSaveLikesForStyleBoards(List<Like> likes);
    List<Long> batchSaveLikesForStyleBoardComments(List<Like> likes);

}
