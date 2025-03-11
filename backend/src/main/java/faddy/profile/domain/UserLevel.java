package faddy.profile.domain;

public enum UserLevel {
    LEVEL_1(1, "패션입문자", 0, 0),
    LEVEL_2(2, "패션풋내기", 1, 1),
    LEVEL_3(3, "패션센스굿", 50, 200),
    LEVEL_4(4, "패션트랜더", 200, 500),
    LEVEL_5(5, "인플루언서", 500, 1000);

    private final int level;
    private final String description;
    private final int requiredPostCount;
    private final int requiredFollowerCount;

    UserLevel(int level, String description, int requiredPostCount, int requiredFollowerCount) {
        this.level = level;
        this.description = description;
        this.requiredPostCount = requiredPostCount;
        this.requiredFollowerCount = requiredFollowerCount;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public int getRequiredPostCount() {
        return requiredPostCount;
    }

    public int getRequiredFollowerCount() {
        return requiredFollowerCount;
    }

    public UserLevel getNextLevel() {
        // Find the next level based on the current level
        int nextOrdinal = this.ordinal() + 1;
        if (nextOrdinal >= UserLevel.values().length) {

            return null;
        } else {
            return UserLevel.values()[nextOrdinal];
        }
    }
}