package faddy.styleBoard.service.useCase;

public interface StyleBoardDeleteService {

    /**
     * 스타일보드 삭제 로직 수행
     * @param authorization 클라이언트 request accessToken
     * @param styleBoardId 삭제할 스타일보드 ID
     * */
    void deleteStyleBoard(String authorization , Long styleBoardId);
}
