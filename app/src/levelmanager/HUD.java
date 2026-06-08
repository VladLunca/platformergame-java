package levelmanager;

import entity.player.Player;
import graphics.assets.Assets;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class HUD {

    public void draw(Graphics g, Player player) {
        BufferedImage[] hearts   = Assets.get("lives");
        BufferedImage fullHeart  = hearts[0];
        BufferedImage halfHeart  = hearts[1];
        BufferedImage emptyHeart = hearts[2];
        int heartSize = 36;
        int startX    = 12;
        int startY    = 12;
        int spacing   = 36;
        float hp = player.getHealth();
        for (int i = 0; i < 5; i++) {
            BufferedImage img;
            if (i + 1 <= hp)  img = fullHeart;
            else if (i < hp)  img = halfHeart;
            else               img = emptyHeart;
            g.drawImage(img, startX + i * spacing, startY, heartSize, heartSize, null);
        }
    }
}
