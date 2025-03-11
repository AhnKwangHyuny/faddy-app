package faddy.global.Utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    /**
     * 지정된 키에 해당하는 데이터를 Redis에서 가져오는 메서드.
     *
     * @param key Redis에서 가져올 데이터의 키
     * @return 키에 해당하는 데이터
     */
    public String getData(String key){
        ValueOperations<String,String> valueOperations=redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * 지정된 키에 값을 저장하는 메서드.
     *
     * @param key   저장할 데이터의 키
     * @param value 저장할 값
     */
    public void setData(String key,String value){
        ValueOperations<String,String> valueOperations=redisTemplate.opsForValue();
        valueOperations.set(key,value);
    }

    /**
     * 지정된 키에 값을 저장하고, 지정된 시간 후에 데이터가 만료되도록 설정하는 메서드.
     *
     * @param key      저장할 데이터의 키
     * @param value    저장할 값
     * @param duration 만료 시간(초)
     */
    public void setDataExpire(String key,String value,long duration){
        ValueOperations<String,String> valueOperations=redisTemplate.opsForValue();
        Duration expireDuration=Duration.ofSeconds(duration);
        valueOperations.set(key,value,expireDuration);
    }

    /**
     * 지정된 키에 해당하는 데이터를 Redis에서 삭제하는 메서드.
     *
     * @param key 삭제할 데이터의 키
     */
    public void deleteData(String key){
        redisTemplate.delete(key);
    }

    /**
     * redis db에 값이 있는지 확인
     *
     * @param key   저장한 데이터의 키
     * @Return 데이터 존재 유무 boolean 형식으로 리턴
     */

    public boolean checkExistsValue(final String key) {
        return this.getData(key) != null;
    }

    /**
     * Redis에서 지정된 키가 존재하는지 확인하는 메서드.
     *
     * @param key 확인할 키
     * @return 키가 존재하면 true, 그렇지 않으면 false
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     *  chatRoom User 정보를 Redis에 저장
     *
     * */
}
