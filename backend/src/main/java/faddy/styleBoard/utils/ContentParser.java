package faddy.styleBoard.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ContentParser {

    private static final Pattern IMAGE_URL_PATTERN = Pattern.compile("<img[^>]+src=\"([^\"]+)\"[^>]*>");

    public static List<String> extractImageUrls(String content) {
        Matcher matcher = IMAGE_URL_PATTERN.matcher(content);
        return matcher.results()
                .map(matchResult -> matchResult.group(1))
                .collect(Collectors.toList());
    }
}
