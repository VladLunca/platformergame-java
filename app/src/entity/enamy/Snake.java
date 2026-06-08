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
    private SnakeStatus status = SnakeStatus.IDLE;
    private int animationTimer;
    private int offsetX = 10;
    private int offsetY = 8;
    private int spawnX;
    private static final int PATROL_SPEED = 2;
    private int patrolLeft;
    private int patrolRight;
    private int patrolDir = 1;

    public Snake(int mapX, int mapY, int health, int patrolRange) {
        super(mapX, mapY, health);
        this.spawnX      = mapX;
        this.patrolLeft  = mapX - patrolRange * Tile.TILE_WIDTH;
        this.patrolRight = mapX + patrolRange * Tile.TILE_WIDTH;
        animations.put("snakeMovingLeft", Assets.get("snakeMovingLeft"));
        animations.put("snakeMovingRight", Assets.get("snakeMovingRight"));
        animations.put("snakeIdle", Assets.get("snakeIdle"));
        this.hitbox = new Rectangle(40, 50);
        this.damage = 1;
    }
    @Override
    public void update() {
        animationTimer++;
        if (animationTimer >= 10) {
            animationTimer = 0;
            frame++;
            if (frame >= 3) frame = 0;
        }

        if (knockbackVX != 0) {
            mapX += (int) knockbackVX;
            knockbackVX *= 0.88f;
            if (Math.abs(knockbackVX) < 0.1f) knockbackVX = 0;
            status = (knockbackVX > 0) ? SnakeStatus.MOVING_RIGHT : SnakeStatus.MOVING_LEFT;
        } else {
            mapX += patrolDir * PATROL_SPEED;
            if (mapX >= patrolRight) {
                mapX = patrolRight;
                patrolDir = -1;
            } else if (mapX <= patrolLeft) {
                mapX = patrolLeft;
                patrolDir = 1;
            }
            status = (patrolDir > 0) ? SnakeStatus.MOVING_RIGHT : SnakeStatus.MOVING_LEFT;
        }
    }

    @Override
    public void update(map.Map map) {
        update();
    }

    public void onCollision(Entity other) {

    }

    @Override
    public void draw(Graphics g, GameWindow wnd, Player player, boolean debug) {
        if (isDead()) return;

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
        String animKey = switch (status) {
            case MOVING_LEFT  -> "snakeMovingLeft";
            case MOVING_RIGHT -> "snakeMovingRight";
            default           -> "snakeIdle";
        };
        BufferedImage[] anim = Assets.get(animKey);
        int f = Math.min(frame, anim.length - 1);
        g.drawImage(anim[f], screenX, screenY + Tile.TILE_HEIGHT + Tile.TILE_HEIGHT - hitbox.height,
                Tile.TILE_WIDTH, Tile.TILE_HEIGHT, null);
        if (debug) drawHitbox(g);
    }
    @Override
    public void draw(Graphics g, GameWindow wnd, map.Map map, boolean debug) {

    }

    @Override
    public void drawHitbox(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(cameraX + offsetX , cameraY + offsetY + Tile.TILE_HEIGHT + Tile.TILE_HEIGHT - hitbox.height ,hitbox.width,hitbox.height);
    }

    @Override
    public Rectangle getScreenHitbox() {
        return new Rectangle(cameraX + offsetX,
                cameraY + offsetY + 2 * Tile.TILE_HEIGHT - hitbox.height,
                hitbox.width, hitbox.height);
    }

    @Override
    public void reset(int startX, int startY, int startHealth) {
        super.reset(startX, startY, startHealth);
        int range = (patrolRight - spawnX) / Tile.TILE_WIDTH;
        this.spawnX        = startX;
        this.patrolLeft    = startX - range * Tile.TILE_WIDTH;
        this.patrolRight   = startX + range * Tile.TILE_WIDTH;
        this.patrolDir     = 1;
        this.frame         = 0;
        this.animationTimer = 0;
        this.status        = SnakeStatus.MOVING_RIGHT;
    }

    public SnakeStatus getStatus() {
        return status;
    }

    public void setStatus(SnakeStatus status) {
        this.status = status;
    }
}
