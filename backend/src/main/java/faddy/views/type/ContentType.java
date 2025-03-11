package faddy.views.type;

public enum ContentType {
    SNAP,
    STYLE_BOARD;

    public static ContentType fromString(String text) {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (i > 0 && Character.isUpperCase(c)) {
                builder.append('_');
            }
            builder.append(Character.toUpperCase(c));
        }

        String formattedText = builder.toString();

        try {
            return ContentType.valueOf(formattedText);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("알 수 없는 객체 타입입니다: " + formattedText);
        }
    }
}
