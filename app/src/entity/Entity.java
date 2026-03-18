package entity;

import entity.player.Player;
import entity.utils.EntitySatus;
import gamewindow.GameWindow;

import java.awt.*;
import java.util.Map;

public abstract class Entity {
    protected int mapX;
    protected int mapY;
    protected int cameraX;
    protected int cameraY;
    protected int speed;
    protected int health;
    protected Rectangle hitbox = new Rectangle();
    protected EntitySatus satus = EntitySatus.RIGHT;

    public Entity(int mapx, int mapy,int health) {
        this.mapX = mapx;
        this.mapY = mapy;
        this.health = health;
    }
    public abstract void update();
    public abstract void draw(Graphics g, GameWindow wnd, Player p);

    public abstract void draw(Graphics g, GameWindow wnd, map.Map map);

    public abstract void drawHitbox(Graphics g, GameWindow wnd);

    public int  getMapX() {
        return mapX;
    }
    public int getMapY() {
        return mapY;
    }

    public int getCameraX() {
        return cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    public int getHealth() {
        return health;
    }
}

