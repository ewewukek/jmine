package ewewukek.jmine;

import java.util.Random;

public class MineField {
    public final int width;
    public final int height;
    public final int mineCount;

    private final int[] field;

    private int flagCount;
    private int closedCount;

    private boolean firstMove;
    private boolean gameOver;
    private boolean dead;

    private static final int NUMBER_MASK = 15;
    private static final int MINE = 16;
    private static final int FLAG = 32;
    private static final int OPEN = 64;

    public MineField(DifficultySetting setting) {
        width = setting.width;
        height = setting.height;
        mineCount = setting.mineCount;
        field = new int[width * height];
        closedCount = field.length;

        firstMove = true;
    }

    public boolean gameStarted() {
        return !firstMove;
    }

    public boolean gameIsOver() {
        return gameOver;
    }

    public boolean isDead() {
        return dead;
    }

    public int getFlagCount() {
        return flagCount;
    }

    public int getNumber(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return field[idx(x, y)] & NUMBER_MASK;
        } else {
            return 0;
        }
    }

    public boolean isOpen(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return (field[idx(x, y)] & OPEN) != 0;
        } else {
            return false;
        }
    }

    public boolean isFlagged(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return (field[idx(x, y)] & FLAG) != 0;
        } else {
            return false;
        }
    }

    public boolean isMine(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return (field[idx(x, y)] & MINE) != 0;
        } else {
            return false;
        }
    }

    public void open(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height
            || isOpen(x, y) || isFlagged(x, y)) return;

        field[idx(x, y)] |= OPEN;
        closedCount--;

        if (firstMove) {
            placeMines();
            firstMove = false;
        }

        if (isMine(x, y)) {
            gameOver = true;
            dead = true;
        } else {
            if (getNumber(x, y) == 0) {
                openAround(x, y);
                return;
            }
            if (closedCount == mineCount) {
                gameOver = true;
                if (flagCount < mineCount) {
                    for (int y1 = 0; y1 < height; y1++) {
                        for (int x1 = 0; x1 < width; x1++) {
                            if (!isOpen(x1, y1) && !isFlagged(x1, y1)) flipFlag(x1, y1);
                        }
                    }
                }
            }
        }
    }

    public void openAround(int x, int y) {
        open(x-1, y-1);
        open(x, y-1);
        open(x+1, y-1);
        open(x-1, y);
        open(x+1, y);
        open(x-1, y+1);
        open(x, y+1);
        open(x+1, y+1);
    }

    public boolean flagsMatchNumber(int x, int y) {
        if (!isOpen(x, y)) return false;

        int flagCount = 0;
        if (isFlagged(x-1, y-1)) flagCount++;
        if (isFlagged(x, y-1)) flagCount++;
        if (isFlagged(x+1, y-1)) flagCount++;
        if (isFlagged(x-1, y)) flagCount++;
        if (isFlagged(x+1, y)) flagCount++;
        if (isFlagged(x-1, y+1)) flagCount++;
        if (isFlagged(x, y+1)) flagCount++;
        if (isFlagged(x+1, y+1)) flagCount++;

        return flagCount == getNumber(x, y);
    }

    public void flipFlag(int x, int y) {
        if (isFlagged(x, y)) {
            field[idx(x, y)] &= ~FLAG;
            flagCount--;
        } else if (!isOpen(x, y)) {
            field[idx(x, y)] |= FLAG;
            flagCount++;
        }
    }

    private int idx(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException(x + ", " + y);
        }
        return x + y * width;
    }

    private void placeMines() {
        int count = 0;
        int[] indexes = new int[field.length];
        for (int i = 0; i < field.length; i++) {
            if ((field[i] & OPEN) == 0) {
                indexes[count] = i;
                count++;
            }
        }

        Random random = new Random();
        for (int m = 0; m < mineCount; m++) {
            int i = random.nextInt(count);
            field[indexes[i]] |= MINE;
            count--;
            indexes[i] = indexes[count];
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isMine(x, y)) {
                    if (y > 0) {
                        if (x > 0) field[idx(x-1, y-1)]++;
                        field[idx(x, y-1)]++;
                        if (x < width-1) field[idx(x+1, y-1)]++;
                    }
                    if (x > 0) field[idx(x-1, y)]++;
                    if (x < width-1) field[idx(x+1, y)]++;
                    if (y < height-1) {
                        if (x > 0) field[idx(x-1, y+1)]++;
                        field[idx(x, y+1)]++;
                        if (x < width-1) field[idx(x+1, y+1)]++;
                    }
                }
            }
        }
    }
}
