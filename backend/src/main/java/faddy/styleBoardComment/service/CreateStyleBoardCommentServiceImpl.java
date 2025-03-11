package faddy.styleBoardComment.service;

import faddy.global.exception.CommentNotFoundException;
import faddy.global.exception.SaveEntityException;
import faddy.like.service.useCase.LikeRedisService;
import faddy.like.type.ContentType;
import faddy.styleBoard.domain.StyleBoard;
import faddy.styleBoard.service.useCase.StyleBoardDetailService;
import faddy.styleBoardComment.domain.StyleBoardComment;
import faddy.styleBoardComment.dto.request.StyleBoardCommentCreateRequestDTO;
import faddy.styleBoardComment.dto.response.create.StyleBoardCommentCreateResponseDTO;
import faddy.styleBoardComment.dto.response.find.StyleBoardReplyResponseDTO;
import faddy.styleBoardComment.repository.StyleBoardCommentJpaRepository;
import faddy.styleBoardComment.service.useCase.CreateStyleBoardCommentService;
import faddy.styleBoardComment.service.useCase.StyleBoardCommentRedisService;
import faddy.styleBoardComment.utils.StyleBoardCommentMapper;
import faddy.user.domain.User;
import faddy.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CreateStyleBoardCommentServiceImpl implements CreateStyleBoardCommentService {

    private final StyleBoardCommentJpaRepository styleBoardCommentRepository;

    private final StyleBoardDetailService styleBoardDetailService;
    private final UserService userService;
    private final StyleBoardCommentRedisService styleBoardCommentRedisService;

    private final LikeRedisService likeRedisService;

    //Error message
    private static final String COMMENT_NOT_FOUND = "댓글이 존재하지 않습니다.";

    @Override
    public StyleBoardCommentCreateResponseDTO create(StyleBoardCommentCreateRequestDTO commentDto ,
                                                     HttpServletRequest request , Long styleBoardId) {
        try {
            // StyleBoard 조회 후 댓글 생성 (동시성 , 정합성)
            StyleBoard styleBoard = styleBoardDetailService.getStyleBoard(styleBoardId);

            // 유저 조회
            String token = request.getHeader("Authorization");
            User author = userService.findUserByToken(token);

            //comment 생성
            StyleBoardComment comment = StyleBoardComment.builder()
                    .content(commentDto.getContent())
                    .styleBoard(styleBoard)
                    .author(author)
                    .build();


            //comment 저장
            StyleBoardComment savedComment = styleBoardCommentRepository.save(comment);

            //like 초기화
            likeRedisService.initializeLikes(savedComment.getId(), ContentType.STYLE_BOARD_COMMENT);

            //redis에 댓글 저장
            styleBoardCommentRedisService.saveStyleBoardComment(styleBoardId , savedComment.getId());

            // comment to response
            return StyleBoardCommentMapper.toDto(savedComment);

        } catch (Exception e) {
            throw new SaveEntityException(HttpStatus.BAD_REQUEST , e.getMessage());
        }
    }



    /**
    * @title 대댓글 생성 전 댓글 밍 styleBoard 데이터 유효성 검증 (동시성 , 정합성)
    * */
    @Override
    @Transactional
    public StyleBoardReplyResponseDTO createReply(StyleBoardCommentCreateRequestDTO commentDto, HttpServletRequest request, Long styleBoardId, Long parentId) {
        try {
            // styleBoard 조회
            StyleBoard styleBoard = styleBoardDetailService.getStyleBoard(styleBoardId);

            //유저 조회
            String token = request.getHeader("Authorization");
            User author = userService.findUserByToken(token);

            // 부모 댓글 조회
            StyleBoardComment parent = styleBoardCommentRepository.findById(parentId)
                    .orElseThrow(() -> new CommentNotFoundException(HttpStatus.BAD_REQUEST.value(), COMMENT_NOT_FOUND));

            // 대댓글 생성 [연관관계 설정]
            StyleBoardComment reply = StyleBoardComment.createReply(parent, author, commentDto.getContent(), styleBoard);

            // 대댓글 저장
            StyleBoardComment savedReply = styleBoardCommentRepository.save(reply);

            //like 초기화
            likeRedisService.initializeLikes(savedReply.getId(), ContentType.STYLE_BOARD_COMMENT);

            return StyleBoardReplyResponseDTO.from(savedReply , 0 , false);

        } catch (Exception e) {
            log.error("Exception occurred while creating comment: ", e);
            throw new SaveEntityException(HttpStatus.BAD_REQUEST , e.getMessage());
        }
    }
}
