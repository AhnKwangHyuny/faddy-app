package faddy.views.service.adapter.useCase;

public interface ViewCountSyncAdapter {

    /**
     * Redis에 저장된 조회수를 MySQL 데이터베이스로 업데이트합니다.
     * 이 메서드는 주기적으로 실행되어 각 콘텐츠의 조회수를 동기화합니다.
     *
     * 구현 클래스는 이 메서드 내에서 Redis로부터 조회수를 조회하고,
     * 해당 조회수를 MySQL 데이터베이스의 관련 엔티티에 저장하는 로직을 포함해야 합니다.
     */
    void updateViewCountsInDatabase();
}
