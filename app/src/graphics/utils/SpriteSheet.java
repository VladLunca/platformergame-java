package graphics.utils;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    private BufferedImage spriteSheet;              /*!< Referinta catre obiectul BufferedImage ce contine sprite sheet-ul.*/
    private static final int    tileWidth   = 64;   /*!< Latimea unei dale din sprite sheet.*/
    private static final int    tileHeight  = 64;

    public SpriteSheet(BufferedImage spriteSheet) {
        this.spriteSheet = spriteSheet;
    }
    public BufferedImage crop(int x, int y)
    {
        return spriteSheet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);// pe o singura imagine sunt mai multe "dale"
    }
    public BufferedImage crop(int x, int y, int width, int height)
    {
        return spriteSheet.getSubimage(x * width, y * height, width, height);// pe o singura imagine sunt mai multe "dale"
    }
}
