package faddy.category.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import faddy.type.ContentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class CategoryPairDto {

    private ContentType contentType;

    @JsonProperty("categories")
    private List<Map<String , String>> categoryPairs;

}
