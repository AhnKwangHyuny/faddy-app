package faddy.styleBoard.service.useCase;


import faddy.styleBoard.dto.request.StyleBoardEditRequestDTO;
import org.springframework.transaction.annotation.Transactional;

public interface StyleBoardEditService {

    @Transactional
    void updateStyleBoard(StyleBoardEditRequestDTO request , Long styleBoardId);
}