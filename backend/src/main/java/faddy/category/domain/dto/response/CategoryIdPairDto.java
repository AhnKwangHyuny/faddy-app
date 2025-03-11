package faddy.category.domain.dto.response;

import faddy.type.ContentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@Setter
public class CategoryIdPairDto {

    private ContentType contentType;
    private List<Map<Long, Long>> linkedCategoryPairSet = new ArrayList<>();


}