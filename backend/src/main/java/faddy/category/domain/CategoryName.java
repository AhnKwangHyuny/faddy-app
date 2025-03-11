package faddy.category.domain;

import faddy.global.exception.ExceptionCode;
import faddy.global.exception.ServerProcessingException;
import faddy.type.ContentType;

import java.util.*;

public enum CategoryName {
    // SNAP 콘텐츠에 대한 카테고리 이름
    SNAP_GENDER("성별", ContentType.SNAP),
    SNAP_SEASON("계절", ContentType.SNAP),
    SNAP_AGE("나이", ContentType.SNAP),

    SNAP_STYLE("스타일" , ContentType.SNAP);


    // TALK 콘텐츠에 대한 카테고리 이름


    private final String value;
    private final ContentType contentType;

    CategoryName(String value, ContentType contentType) {
        this.value = value;
        this.contentType = contentType;
    }

    public String getValue() {
        return value;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public static List<CategoryName> fromContentType(ContentType contentType) {
        List<CategoryName> matchingNames = new ArrayList<>();
        for (CategoryName categoryName : values()) {
            if (categoryName.getContentType() == contentType) {
                matchingNames.add(categoryName);
            }
        }
        if (matchingNames.isEmpty()) {
            throw new ServerProcessingException(ExceptionCode.FAIL_SAVE_ENTITY);
        }
        return matchingNames;
    }

    /**
     * @title : 클라이언트로 부터 받은 카테고리 네임이 카테고리 리스트에 있는지 확인 (없다면 null 반환)
     * */
    public static CategoryName fromValue(String value , ContentType contentType) {
        for (CategoryName categoryName : values()) {
            if (categoryName.getValue().equals(value) && categoryName.getContentType().equals(contentType)) {
                return categoryName;
            }
        }
        return null;
    }
}
