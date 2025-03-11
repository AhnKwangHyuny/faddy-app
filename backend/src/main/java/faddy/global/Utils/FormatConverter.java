package faddy.global.Utils;

// FormatConverter.java

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FormatConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 중 오류가 발생하였습니다.", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.warn("JSON 역변환 중 오류가 발생하였습니다.", e);
            throw new RuntimeException("JSON 역변환 중 오류가 발생하였습니다.", e);
        }
    }
}
