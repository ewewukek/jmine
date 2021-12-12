package ewewukek.jmine;

import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;

public class SpriteSheet extends Sprite {
    public final int spriteWidth;
    public final int spriteHeight;

    public SpriteSheet(String path, int spriteCount) throws IOException {
        super(path);
        spriteWidth = width / spriteCount;
        spriteHeight = height;
    }

    public void draw(Graphics g, Point p, int index) {
        draw(g, p.x, p.y, index);
    }

    public void draw(Graphics g, int x, int y, int index) {
        g.drawImage(image, x, y, x+spriteWidth, y+spriteHeight,
            spriteWidth*index, 0, spriteWidth*(index+1), spriteHeight, null);
    }
}
