package faddy.hashTags.domain.dto.request;

import faddy.hashTags.types.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HashTagRequestDto {
    private ContentType contentType;
    private List<String> tags;

    @Override
    public String toString() {
        return "HashTagRequestDto{" +
                "contentType=" + contentType +
                ", tags=" + tags +
                '}';
    }
}
