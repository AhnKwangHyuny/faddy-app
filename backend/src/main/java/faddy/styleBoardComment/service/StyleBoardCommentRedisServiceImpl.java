package faddy.styleBoardComment.service;

import faddy.global.exception.BadRequestException;
import faddy.global.exception.SaveEntityException;
import faddy.styleBoardComment.service.useCase.StyleBoardCommentRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StyleBoardCommentRedisServiceImpl implements StyleBoardCommentRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private String generateRedisKey(Long styleBoardId) {
        return "styleBoard:" + styleBoardId + ":styleBoardComment";
    }

    @Override
    @Transactional
    public void initializeStyleBoardComments(Long styleBoardId) {
        String redisKey = generateRedisKey(styleBoardId);
        redisTemplate.opsForSet().add(redisKey, "initialized");
    }

    @Override
    @Transactional
    public void saveStyleBoardComment(Long styleBoardId, Long styleBoardCommentId) {
        try {
            String redisKey = generateRedisKey(styleBoardId);
            Long result = redisTemplate.opsForSet().add(redisKey, styleBoardCommentId.toString());
            if (result == 0) {
                throw new SaveEntityException(HttpStatus.BAD_REQUEST, "이미 댓글이 등록되어 있습니다.");
            }
        } catch (Exception e) {
            throw new SaveEntityException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    @Transactional
    public void removeStyleBoardComment(Long styleBoardId, Long styleBoardCommentId) {
        try {
            String redisKey = generateRedisKey(styleBoardId);
            redisTemplate.opsForSet().remove(redisKey, styleBoardCommentId.toString());
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), "[redis] 댓글 삭제에 실패했습니다.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int countStyleBoardComments(Long styleBoardId) {
        String redisKey = generateRedisKey(styleBoardId);
        try {
            Set<Object> members = redisTemplate.opsForSet().members(redisKey);
            if (members.contains("initialized")) {
                return members.size() - 1; // "initialized" 값을 제외한 개수
            } else {
                return members.size();
            }
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getStyleBoardCommentIds(Long styleBoardId) {
        String redisKey = generateRedisKey(styleBoardId);
        try {
            Set<Object> members = redisTemplate.opsForSet().members(redisKey);
            members.remove("initialized"); // "initialized" 값은 제외
            return members.stream()
                    .map(member -> Long.parseLong((String) member))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "[redis] 댓글 목록 조회에 실패했습니다.");
        }
    }

    @Override
    @Transactional
    public void deleteStyleBoardComments(Long styleBoardId) {
        try {
            String redisKey = generateRedisKey(styleBoardId);
            redisTemplate.delete(redisKey);
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
