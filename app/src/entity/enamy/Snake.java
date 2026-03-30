package entity.enamy;

import entity.Entity;
import entity.player.Player;
import entity.utils.SnakeStatus;
import gamewindow.GameWindow;
import graphics.assets.Assets;
import graphics.tiles.Tile;
import utils.TileID;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Snake extends Entity {

    private Map<String, BufferedImage[]> animations = new HashMap<String, BufferedImage[]>();
    private int frame=0;
    private SnakeStatus status =  SnakeStatus.IDLE;
    private int animationTimer;
    private int offsetX = 10;
    private int offsetY = 8; // se vor folosi si pt a vedea suprapunerea la collision

    public Snake(int mapX, int mapY, int health) {
        super(mapX, mapY, health);
        animations.put("snakeMovingLeft", Assets.get("snakeMovingLeft"));
        animations.put("snakeMovingRight", Assets.get("snakeMovingRight"));
        animations.put("snakeIdle", Assets.get("snakeIdle"));
        this.hitbox = new Rectangle( 40, 50);
    }
    @Override
    public void update() {
        animationTimer++;
        if (animationTimer >= 10) {
            animationTimer = 0;
            frame++;
            if (frame >= 3) {
                frame = 0;
            }
        }
    }

    @Override
    public void update(map.Map map) {

    }

    public void onCollision(Entity other) {

    }

    @Override
    public void draw(Graphics g, GameWindow wnd, Player player) {

        int camX = player.getMapX() - wnd.GetWndWidth() / 2;
        int camY = player.getMapY() - wnd.GetWndHeight() / 2;

        if (mapX < camX - Tile.TILE_WIDTH || mapX > camX + wnd.GetWndWidth() + Tile.TILE_WIDTH ||
                mapY < camY - Tile.TILE_HEIGHT || mapY > camY + wnd.GetWndHeight() + Tile.TILE_HEIGHT) {
            return;
        }

        int screenX = mapX - player.getMapX() + player.getCameraX();
        int screenY = mapY - player.getMapY() + player.getCameraY() + Tile.TILE_HEIGHT;
        this.cameraX = screenX;
        this.cameraY = screenY;
        if (frame < Assets.get("snakeIdle").length) {
            g.drawImage(Assets.get("snakeIdle")[frame], screenX, screenY + Tile.TILE_HEIGHT + Tile.TILE_HEIGHT - hitbox.height,
                    Tile.TILE_WIDTH, Tile.TILE_HEIGHT, null);
        }
        drawHitbox(g);
    }
    @Override
    public void draw(Graphics g, GameWindow wnd, map.Map map) {

    }

    @Override
    public void drawHitbox(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(cameraX + offsetX , cameraY + offsetY + Tile.TILE_HEIGHT + Tile.TILE_HEIGHT - hitbox.height ,hitbox.width,hitbox.height);
    }

    public SnakeStatus getStatus() {
        return status;
    }

    public void setStatus(SnakeStatus status) {
        this.status = status;
    }
}
