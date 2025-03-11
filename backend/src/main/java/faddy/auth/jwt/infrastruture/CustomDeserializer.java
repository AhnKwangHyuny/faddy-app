package faddy.auth.jwt.infrastruture;

import com.nimbusds.jose.shaded.gson.Gson;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.io.Deserializer;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class CustomDeserializer implements Deserializer<Map<String, ?>> {

    private final Gson gson = new Gson();

    @Override
    public Map<String, ?> deserialize(byte[] bytes) throws DeserializationException {
        try {
            return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), Map.class);
        } catch (Exception e) {
            throw new DeserializationException("Failed to deserialize data from JSON", e);
        }
    }

    public Map<String, ?> deserialize(Reader reader) throws DeserializationException {
        try {
            return gson.fromJson(reader, Map.class);
        } catch (Exception e) {
            throw new DeserializationException("Failed to deserialize data from JSON", e);
        }
    }
}
