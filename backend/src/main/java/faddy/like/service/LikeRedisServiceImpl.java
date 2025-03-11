package faddy.like.service;

import faddy.global.exception.BadRequestException;
import faddy.global.exception.SaveEntityException;
import faddy.like.service.useCase.LikeRedisService;
import faddy.like.type.ContentType;
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
public class LikeRedisServiceImpl implements LikeRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private String generateRedisKey(ContentType contentType, Long objectId) {
        return "like:" + contentType.name().toLowerCase() + ":" + objectId;
    }

    @Override
    @Transactional
    public void initializeLikes(Long objectId, ContentType contentType) {
        String redisKey = generateRedisKey(contentType, objectId);
        redisTemplate.opsForSet().add(redisKey, "initialized");
    }

    @Override
    @Transactional
    public void saveLike(Long objectId, Long userId, ContentType contentType) {
        try {
            String redisKey = generateRedisKey(contentType, objectId);
            Long result = redisTemplate.opsForSet().add(redisKey, userId.toString());
            if (result == 0) {
                throw new SaveEntityException(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 유저입니다.");
            }
        } catch (Exception e) {
            throw new SaveEntityException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    @Transactional
    public void removeLike(Long objectId, Long userId, ContentType contentType) {
        try {
            String redisKey = generateRedisKey(contentType, objectId);
            redisTemplate.opsForSet().remove(redisKey, userId.toString());
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), "[redis] 좋아요 삭제에 실패했습니다.");
        }
    }

    @Override
    public boolean isLiked(Long objectId, Long userId, ContentType contentType) {
        String redisKey = generateRedisKey(contentType, objectId);
        return redisTemplate.opsForSet().isMember(redisKey, userId.toString());
    }

    @Override
    @Transactional(readOnly = true)
    public int countLikes(Long objectId, ContentType contentType) {
        String redisKey = generateRedisKey(contentType, objectId);
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
    public List<Long> getLikedUserIds(Long objectId, ContentType contentType) {
        String redisKey = generateRedisKey(contentType, objectId);
        try {
            Set<Object> members = redisTemplate.opsForSet().members(redisKey);
            members.remove("initialized"); // "initialized" 값은 제외
            // Set<Object>를 List<Long>으로 변환
            return members.stream()
                    .map(member -> Long.parseLong((String) member))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "[redis] 좋아요 사용자 목록 조회에 실패했습니다.");
        }
    }

    @Override
    @Transactional
    public void deleteLikes(Long objectId, ContentType contentType) {
        try {
            String redisKey = generateRedisKey(contentType, objectId);
            redisTemplate.delete(redisKey);
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value() , e.getMessage());
        }
    }
}
