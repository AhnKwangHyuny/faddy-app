package faddy.styleBoard.service;

import faddy.image.service.ImageService;
import faddy.like.service.useCase.LikeRedisService;
import faddy.log.exception.ExceptionLogger;
import faddy.styleBoard.domain.StyleBoard;
import faddy.styleBoard.dto.request.StyleBoardCreateDTO;
import faddy.styleBoard.repository.StyleBoardJpaRepository;
import faddy.styleBoard.service.useCase.StyleBoardCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StyleBoardCreateServiceImpl implements StyleBoardCreateService {

    private final StyleBoardJpaRepository styleBoardRepository;
    private final ImageService imageService;

    @Override
    public StyleBoard createStyleBoardEntity(StyleBoardCreateDTO request) {
        try {

            return StyleBoard.builder()
                    .withCategory(request.getCategory())
                    .withTitle(request.getTitle())
                    .withContent(request.getContent())
                    .build();
        } catch (Exception e) {
            ExceptionLogger.logException(e);
            throw e;
        }
    }
}
