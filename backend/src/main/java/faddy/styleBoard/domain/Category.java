package faddy.styleBoard.domain;

public enum Category {
    QUESTION("질문하기"),
    DAILY_SHARE("일상공유"),
    FASHION_SHARE("패션공유"),
    STYLE_TIP("스타일팁"),
    NEWS_SHARE("소식공유");
    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }


    public static Category fromUrlValue(String urlValue) {

        //urlValue가 null이거나 비어있으면 null 반환
        if (urlValue == null || urlValue.isEmpty()) {
            return null;
        }

        switch (urlValue.toLowerCase()) {
            case "question":
                return QUESTION;
            case "daily":
                return DAILY_SHARE;
            case "fashion":
                return FASHION_SHARE;
            case "tips":
                return STYLE_TIP;
            case "news":
                return NEWS_SHARE;
            default:
                return null;
        }
    }

}
