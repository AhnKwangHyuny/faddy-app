package faddy.styleBoard.service.adapter;

import faddy.global.exception.SaveEntityException;
import faddy.hashTags.domain.HashTag;
import faddy.hashTags.dto.request.HashTagRequestDTO;
import faddy.hashTags.service.HashTagService;
import faddy.image.domain.Image;
import faddy.image.dto.request.ImageLookupRequestDTO;
import faddy.image.service.ImageService;
import faddy.like.service.useCase.LikeRedisService;
import faddy.like.type.ContentType;
import faddy.log.exception.ExceptionLogger;
import faddy.styleBoard.domain.StyleBoard;
import faddy.styleBoard.dto.request.StyleBoardCreateDTO;
import faddy.styleBoard.dto.request.StyleBoardRequestDTO;
import faddy.styleBoard.repository.StyleBoardJpaRepository;
import faddy.styleBoard.service.adapter.useCase.StyleBoardCreatePersistenceAdaptor;
import faddy.styleBoard.service.useCase.StyleBoardCreateService;
import faddy.styleBoard.utils.StyleBoardRequestParser;
import faddy.user.domain.User;
import faddy.user.service.UserService;
import faddy.views.service.useCase.ViewRedisService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StyleBoardCreatePersistenceAdaptorImpl implements StyleBoardCreatePersistenceAdaptor {

    private final UserService userService;
    private final StyleBoardCreateService styleBoardCreateService;
    private final ImageService imageService;
    private final HashTagService hashTagService;
    private final LikeRedisService likeRedisService;
    private final ViewRedisService viewRedisService;

    private final StyleBoardJpaRepository styleBoardRepository;

    @Override
    public Long create(StyleBoardRequestDTO styleBoardRequestDTO , HttpServletRequest request) {

        try {

            // 작성자 조회
            User authorization = userService.findUserByToken(request.getHeader("Authorization"));

            log.info("Authorization user: {}", authorization);

            //styleBoard Entity 생성
            StyleBoardCreateDTO styleBoardCreateDTO = StyleBoardRequestParser.toStyleBoardCreateDTO(styleBoardRequestDTO);
            StyleBoard styleBoard = styleBoardCreateService.createStyleBoardEntity(styleBoardCreateDTO);

            //Image 조회
            List<Image> images = findImages(styleBoardRequestDTO);


            //HashTag 엔티티 생성
            List<HashTag> hashTags = saveHashTags(styleBoardRequestDTO);

            //연관관계 매핑
            associate(styleBoard, images, hashTags, authorization);

            //styleBoard 저장 (다른 연관관계 dirty checking)
            StyleBoard saved = styleBoardRepository.save(styleBoard);

            //styleBoard like 초기화
            likeRedisService.initializeLikes(saved.getId(), ContentType.STYLE_BOARD);

            //styleBoard 조회수 초기화
            faddy.views.type.ContentType contentType = faddy.views.type.ContentType.STYLE_BOARD;
            viewRedisService.initializeViews(saved.getId(), contentType);

            return saved.getId();

        } catch (Exception e) {
            ExceptionLogger.logException(e);
            throw new SaveEntityException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "StyleBoard Entity 생성 실패", e);
        }
    }

    private void associate(StyleBoard styleBoard, List<Image> images, List<HashTag> hashTags, User user) {
        if (images != null) {
            for (Image image : images) {
                image.linkStyleBoard(styleBoard);
            }
        }

        if (hashTags != null) {
            for (HashTag hashTag : hashTags) {
                hashTag.linkToStyleBoard(styleBoard);
            }
        }

        styleBoard.linkToAuthor(user);
    }

    private List<Image> findImages(StyleBoardRequestDTO styleBoardRequestDTO) {
        List<ImageLookupRequestDTO> imageUrls = StyleBoardRequestParser.extractImageLookupRequestDTOs(styleBoardRequestDTO.getContent());
        return imageService.findByImageUrl(imageUrls);
    }

    private List<HashTag> saveHashTags(StyleBoardRequestDTO styleBoardRequestDTO) {
        List<HashTagRequestDTO> hashTagDtos = StyleBoardRequestParser.extractHashTagRequestDTOs(styleBoardRequestDTO.getHashTags());
        return hashTagService.saveHashTags(hashTagDtos);
    }
}
