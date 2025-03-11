package faddy.auth.jwt.infrastruture;

import com.nimbusds.jose.shaded.gson.Gson;
import io.jsonwebtoken.io.SerializationException;
import io.jsonwebtoken.io.Serializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class CustomSerializer implements Serializer<Map<String, ?>> {

    private final Gson gson = new Gson();

    @Override
    public byte[] serialize(Map<String, ?> data) {
        return gson.toJson(data).getBytes(StandardCharsets.UTF_8);
    }

    public void serialize(Map<String, ?> data, OutputStream outputStream) throws SerializationException {
        try {
            outputStream.write(gson.toJson(data).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new SerializationException("Failed to serialize data to JSON", e);
        }
    }
}
