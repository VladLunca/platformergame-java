package entity.prop;

import entity.player.Player;
import graphics.assets.Assets;
import graphics.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Treasure implements LevelGoal {
    private final int mapX, mapY;
    private int screenX, screenY;
    private boolean activated = false;

    public Treasure(int mapX, int mapY) {
        this.mapX = mapX;
        this.mapY = mapY;
    }

    public void draw(Graphics g, Player player, boolean dragonsAllDead) {
        screenX = mapX - player.getMapX() + player.getCameraX();
        screenY = mapY - player.getMapY() + player.getCameraY() + Tile.TILE_HEIGHT;

        BufferedImage img = Assets.get("treasure")[0];
        g.drawImage(img, screenX, screenY, Tile.TILE_WIDTH, Tile.TILE_HEIGHT, null);

        if (dragonsAllDead && !activated) {
            g.setColor(new Color(255, 215, 0, 120));
            g.drawRect(screenX, screenY, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
        }
    }

    public boolean tryActivate(Player player, boolean dragonsAllDead) {
        if (!dragonsAllDead || activated) return false;
        Rectangle playerBox  = player.getScreenHitbox();
        Rectangle treasureBox = new Rectangle(screenX, screenY, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
        if (playerBox.intersects(treasureBox)) {
            activated = true;
            return true;
        }
        return false;
    }

    public void reset()             { activated = false; }
    public boolean isActivated()    { return activated; }
}
