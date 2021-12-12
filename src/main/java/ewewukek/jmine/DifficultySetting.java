package ewewukek.jmine;

public enum DifficultySetting {
    BEGINNER(9, 9, 10),
    INTERMEDIATE(16, 16, 40),
    EXPERT(30, 16, 99);

    public final int width;
    public final int height;
    public final int mineCount;

    DifficultySetting(int width, int height, int mineCount) {
        this.width = width;
        this.height = height;
        this.mineCount = mineCount;
    }
}
