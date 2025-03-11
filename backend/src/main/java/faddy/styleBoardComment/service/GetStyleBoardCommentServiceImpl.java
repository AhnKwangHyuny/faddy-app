package faddy.styleBoardComment.service;

import faddy.global.exception.CommentNotFoundException;
import faddy.like.service.useCase.LikeRedisService;
import faddy.like.type.ContentType;
import faddy.styleBoard.service.useCase.StyleBoardDetailService;
import faddy.styleBoardComment.domain.StyleBoardComment;
import faddy.styleBoardComment.dto.response.find.StyleBoardCommentResponseDTO;
import faddy.styleBoardComment.dto.response.find.StyleBoardReplyResponseDTO;
import faddy.styleBoardComment.repository.StyleBoardCommentJpaRepository;
import faddy.styleBoardComment.service.useCase.GetStyleBoardCommentService;
import faddy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GetStyleBoardCommentServiceImpl implements GetStyleBoardCommentService {

    private final StyleBoardCommentJpaRepository styleBoardCommentRepository;

    private final StyleBoardDetailService styleBoardDetailService;
    private final UserService userService;
    private final LikeRedisService likeRedisService;

    @Override
    @Transactional(readOnly = true)
    public List<StyleBoardCommentResponseDTO> findByStyleBoardIdWithReplies(Long styleBoardId , Long userId) {
        try {
            List<StyleBoardComment> comments = styleBoardCommentRepository.findByStyleBoardIdWithChildren(styleBoardId);
            List<StyleBoardCommentResponseDTO> response = new ArrayList<>();

            for (StyleBoardComment comment : comments) {

                int likeCount = likeRedisService.countLikes(comment.getId(), ContentType.STYLE_BOARD_COMMENT);

                // 비 로그인 유저가 조회 시 isLiked = false
                boolean isLiked = false;

                if(userId != null) {
                    isLiked = likeRedisService.isLiked(comment.getId(),  userId , ContentType.STYLE_BOARD_COMMENT);
                }

                //dto에 comment , like count 매핑
                StyleBoardCommentResponseDTO dto = StyleBoardCommentResponseDTO.from(comment, likeCount , isLiked);

                dto.setReplies(comment.getChildren().stream()
                        .map(reply -> {
                            int replyLikeCount = likeRedisService.countLikes(reply.getId(), ContentType.STYLE_BOARD_COMMENT);

                            // 비 로그인 유저가 조회 시 isLiked = false
                            boolean isReplyLiked = false;

                            if(userId != null) {
                                isReplyLiked = likeRedisService.isLiked(reply.getId(),  userId , ContentType.STYLE_BOARD_COMMENT);
                            }

                            return StyleBoardReplyResponseDTO.from(reply, replyLikeCount , isReplyLiked);
                        })
                        .collect(Collectors.toList()));

                response.add(dto);
            }

            return response;

        } catch (Exception e) {
            throw new CommentNotFoundException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public StyleBoardComment loadStyleBoardById(Long styleBoardId) {
        return styleBoardCommentRepository.findById(styleBoardId)
                .orElseThrow(() -> new CommentNotFoundException(HttpStatus.BAD_REQUEST.value(), "댓글이 존재하지 않습니다. {id : " + styleBoardId + "}"));
    }

    @Override
    public List<StyleBoardComment> loadALlStyleBoardComment() {
        return styleBoardCommentRepository.findAllStyleBoardComment();
    }
}
