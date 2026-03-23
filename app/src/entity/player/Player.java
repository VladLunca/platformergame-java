package entity.player;

import entity.Entity;
import gamewindow.GameWindow;
import graphics.assets.Assets;
import graphics.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Player extends Entity {
    private static Player instance = null;
    private Map<String, BufferedImage[]> animations =  new HashMap<String, BufferedImage[]>();
    private Player( int mapX, int mapY, int health) {
        super(mapX, mapY, health);
        hitbox.setSize(64,64);
        animations.put("playerRunLeft", Assets.get("playerRunLeft"));
        animations.put("playerRunRight",Assets.get("playerRunRight"));
        animations.put("playerAttackRight",Assets.get("playerAttackRight"));
        animations.put("playerAttackLeft",Assets.get("playerAttackLeft"));
        animations.put("playerJumpLeft",Assets.get("playerJumpLeft"));
        animations.put("playerJumpRight",Assets.get("playerJumpRight"));
        animations.put("playerIdleRight",Assets.get("playerIdleRight"));
        animations.put("playerIdleLeft",Assets.get("playerIdleLeft"));
        animations.put("playerHurtRight",Assets.get("playerHurtRight"));
        animations.put("playerHurtLeft",Assets.get("playerHurtLeft"));
        instance = this;
    }

    @Override
    public void update() {

    }

    @Override
    public void update(Map map) {

    }

    public static Player createPlayer(int mapX, int mapY, int health) {
        if(instance == null) {
            return new Player(mapX, mapY, health);
        }
        System.out.println("You can't create 2 players.");
        return instance;
    }
    @Override
    public void update(map.Map map) {

    }

    public void onCollision(Entity other) {}

    @Override
    public void draw(Graphics g, GameWindow wnd, Player p) {

    }

    @Override
    public void draw(Graphics g, GameWindow wnd, map.Map map) {
        cameraX = wnd.GetWndWidth() / 2;
        cameraY = wnd.GetWndHeight() / 2;
        g.drawImage(animations.get("playerIdleLeft")[0],cameraX, cameraY , Tile.TILE_WIDTH,Tile.TILE_HEIGHT,null);
    }
    @Override
    public void drawHitbox(Graphics g, GameWindow wnd){}
}
