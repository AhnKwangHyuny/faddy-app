package faddy.global.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagUtils {

    /**
     * 태그 문자열을 리스트로 변환
     *
     * @param tags 콤마로 구분된 태그 문자열 (예: "casual,sporty")
     * @return 태그 리스트
     */
    public static List<String> convertTagsToList(String tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
