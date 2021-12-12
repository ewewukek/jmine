package ewewukek.jmine;

import java.awt.Graphics;
import java.io.IOException;

public class NinePatch extends Sprite {
    public final int left;
    public final int top;
    public final int right;
    public final int bottom;

    public NinePatch(String path, int left, int top, int right, int bottom) throws IOException {
        super(path);
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void draw(Graphics g, int x1, int y1, int x2, int y2) {
        g.drawImage(image, x1, y1, x1+left, y1+top,
            0, 0, left, top, null);
        g.drawImage(image, x1+left, y1, x2-right, y1+top,
            left+1, 0, width-right-1, top, null);
        g.drawImage(image, x2-right, y1, x2, y1+top,
            width-right, 0, width, top, null);

        g.drawImage(image, x1, y1+top, x1+left, y2-bottom,
            0, top+1, left, height-bottom-1, null);
        g.drawImage(image, x1+left, y1+top, x2-right, y2-bottom,
            left+1, top+1, width-right-1, height-bottom-1, null);
        g.drawImage(image, x2-right, y1+top, x2, y2-bottom,
            width-right, top+1, width, height-bottom-1, null);

        g.drawImage(image, x1, y2-bottom, x1+left, y2,
            0, height-bottom, left, height, null);
        g.drawImage(image, x1+left, y2-bottom, x2-right, y2,
            left+1, height-bottom, width-right-1, height, null);
        g.drawImage(image, x2-right, y2-bottom, x2, y2,
            width-right, height-bottom, width, height, null);
    }
}
