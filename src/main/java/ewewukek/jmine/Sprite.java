package ewewukek.jmine;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Sprite {
    public final int width;
    public final int height;

    protected final BufferedImage image;

    public Sprite(String path) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new IOException("could not load " + path);
        }
        image = ImageIO.read(is);
        width = image.getWidth();
        height = image.getHeight();
    }

    public void draw(Graphics g, Point p) {
        draw(g, p.x, p.y);
    }

    public void draw(Graphics g, int x, int y) {
        g.drawImage(image, x, y, x+width, y+height, 0, 0, width, height, null);
    }
}
