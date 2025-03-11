package faddy.views.service;

import faddy.global.Utils.IpUtils;
import faddy.global.exception.BadRequestException;
import faddy.global.exception.SaveEntityException;
import faddy.log.exception.ExceptionLogger;
import faddy.views.service.useCase.ViewRedisService;
import faddy.views.type.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ViewRedisServiceImpl implements ViewRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private String generateRedisKey(ContentType contentType, Long objectId) {
        return "view:" + contentType.name().toLowerCase() + ":" + objectId;
    }

    @Override
    @Transactional
    public void initializeViews(Long objectId, ContentType contentType) {
        try {
            String redisKey = generateRedisKey(contentType, objectId);
            redisTemplate.opsForSet().add(redisKey, "initialized");
        } catch (Exception e) {
            ExceptionLogger.logException(e);
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), "조회수 초기화에 실패했습니다.");
        }

    }

    @Override
    @Transactional
    public void saveView(Long objectId, Long userId, ContentType contentType) {
        try {
            String redisKey = generateRedisKey(contentType, objectId);
            Long result;

            if (userId != null) { //비로그인 유저 ip 처리
                result = redisTemplate.opsForSet().add(redisKey, userId.toString());
            } else {
                String userIp = IpUtils.getClientIp();
                result = redisTemplate.opsForSet().add(redisKey, userIp);
            }

        } catch (Exception e) {
            throw new SaveEntityException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @Override
    public boolean isViewed(Long objectId, Long userId, ContentType contentType) {
        String redisKey = generateRedisKey(contentType, objectId);
        return redisTemplate.opsForSet().isMember(redisKey, userId.toString());
    }

    @Override
    @Transactional(readOnly = true)
    public int countViews(Long objectId, ContentType contentType) {
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
    @Transactional
    public void deleteViews(Long objectId, ContentType contentType) {
        try {
            String redisKey = generateRedisKey(contentType, objectId);
            redisTemplate.delete(redisKey);
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
