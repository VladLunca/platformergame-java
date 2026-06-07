package entity.enamy;

import entity.Entity;
import entity.player.Player;
import entity.utils.DragonTypes;
import entity.utils.MoveInfo;
import gamewindow.GameWindow;
import graphics.assets.Assets;
import graphics.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Dragon extends Entity {
    private static final int CHASE_SPEED  = 1;
    private static final int ANIM_DELAY   = 12;
    private static final int FRAME_COUNT  = 3;
    private static final int AGGRO_TILES  = 5;
    private static final int AGGRO_RANGE  = AGGRO_TILES * Tile.TILE_WIDTH;

    private DragonTypes dragonType = DragonTypes.GREEN;
    private int animationTimer = 0;
    private int animFrame      = 0;
    private boolean facingLeft = false;

    public Dragon(int mapx, int mapy, int health) {
        super(mapx, mapy, health);
        this.hitbox.setSize(64, 64);
        this.damage = 2;
        this.speed  = CHASE_SPEED;
    }

    @Override
    public void update() {}

    @Override
    public void update(map.Map map) {
        animationTimer++;
        if (animationTimer >= ANIM_DELAY) {
            animationTimer = 0;
            animFrame = (animFrame + 1) % FRAME_COUNT;
        }

        Player player = Player.getInstance();
        if (player == null || player.isDead()) return;

        int dx = player.getMapX() - mapX;
        int dy = player.getMapY() - mapY;

        if (knockbackVX != 0) {
            int newX = mapX + (int) knockbackVX;
            if (canMoveX(newX, map)) mapX = newX;
            knockbackVX *= 0.85f;
            if (Math.abs(knockbackVX) < 0.1f) knockbackVX = 0;
            facingLeft = knockbackVX < 0;
            return;
        }

        int dist = (int) Math.sqrt((double) dx * dx + (double) dy * dy);
        if (dist > AGGRO_RANGE) return;

        facingLeft = dx < 0;
        if (Math.abs(dx) > speed) {
            int newX = mapX + (dx > 0 ? speed : -speed);
            if (canMoveX(newX, map)) mapX = newX;
        }
        if (Math.abs(dy) > speed) {
            int newY = mapY + (dy > 0 ? speed : -speed);
            if (canMoveY(newY, map)) mapY = newY;
        }
    }

    private boolean canMoveX(int newX, map.Map map) {
        return MoveInfo.moveValid(newX, mapY, map)
            && MoveInfo.moveValid(newX + hitbox.width, mapY, map)
            && MoveInfo.moveValid(newX, mapY + hitbox.height - 1, map)
            && MoveInfo.moveValid(newX + hitbox.width, mapY + hitbox.height - 1, map);
    }

    private boolean canMoveY(int newY, map.Map map) {
        return MoveInfo.moveValid(mapX, newY, map)
            && MoveInfo.moveValid(mapX + hitbox.width, newY, map)
            && MoveInfo.moveValid(mapX, newY + hitbox.height - 1, map)
            && MoveInfo.moveValid(mapX + hitbox.width, newY + hitbox.height - 1, map);
    }

    @Override
    public void draw(Graphics g, GameWindow wnd, Player player) {
        int camX = player.getMapX() - wnd.GetWndWidth()  / 2;
        int camY = player.getMapY() - wnd.GetWndHeight() / 2;

        if (mapX < camX - 2 * Tile.TILE_WIDTH  || mapX > camX + wnd.GetWndWidth()  + 2 * Tile.TILE_WIDTH ||
            mapY < camY - 2 * Tile.TILE_HEIGHT || mapY > camY + wnd.GetWndHeight() + 2 * Tile.TILE_HEIGHT) {
            return;
        }

        int screenX = mapX - player.getMapX() + player.getCameraX();
        int screenY = mapY - player.getMapY() + player.getCameraY() + Tile.TILE_HEIGHT;
        this.cameraX = screenX;
        this.cameraY = screenY;

        String prefix = switch (dragonType) {
            case PURPLE -> "dragonPurple";
            case RED    -> "dragonBone";
            default     -> "dragonGreen";
        };
        String animKey = facingLeft ? prefix + "WalkLeft" : prefix + "WalkRight";
        BufferedImage img = Assets.get(animKey)[animFrame];

        int drawW = 2 * Tile.TILE_WIDTH;
        int drawH = 2 * Tile.TILE_HEIGHT;
        g.drawImage(img, screenX - Tile.TILE_WIDTH / 2, screenY - Tile.TILE_HEIGHT, drawW, drawH, null);
        drawHitbox(g);
    }

    @Override
    public void draw(Graphics g, GameWindow wnd, map.Map map) {}

    @Override
    public void drawHitbox(Graphics g) {
        g.setColor(Color.ORANGE);
        Rectangle hb = getScreenHitbox();
        g.drawRect(hb.x, hb.y, hb.width, hb.height);
    }

    @Override
    public Rectangle getScreenHitbox() {
        int W = Tile.TILE_WIDTH;
        int H = Tile.TILE_HEIGHT;
        int hbX = cameraX - W / 2 + (2 * W - hitbox.width)  / 2;
        int hbY = cameraY - H      + (2 * H - hitbox.height) / 2;
        return new Rectangle(hbX, hbY, hitbox.width, hitbox.height);
    }

    @Override
    public void reset(int startX, int startY, int startHealth) {
        super.reset(startX, startY, startHealth);
        animationTimer = 0;
        animFrame      = 0;
        facingLeft     = false;
    }

    public void setDragonType(DragonTypes type) {
        this.dragonType = type;
    }
}
