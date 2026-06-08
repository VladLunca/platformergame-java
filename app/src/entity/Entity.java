package entity;

import entity.player.Player;
import entity.utils.PlayerStatus;
import gamewindow.GameWindow;

import java.awt.*;

public abstract class Entity {
    protected int mapX;
    protected int mapY;
    protected int cameraX;
    protected int cameraY;
    protected int speed;
    protected float health;
    protected int damage;
    protected float knockbackVX;
    protected int frame;
    protected Rectangle hitbox = new Rectangle();

    public Entity(int mapx, int mapy,int health) {
        this.mapX = mapx;
        this.mapY = mapy;
        this.health = health;
    }
    public abstract void update();

    public abstract void update(map.Map map);

    public abstract void draw(Graphics g, GameWindow wnd, Player p, boolean debug);

    public abstract void draw(Graphics g, GameWindow wnd, map.Map map, boolean debug);


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

    public float getHealth() {
        return health;
    }

    public void setCameraX(int i) {
        this.cameraX = i;
    }

    public void setCameraY(int i) {
        this.cameraY = i;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public abstract void drawHitbox(Graphics g);

    public boolean isDead() {
        return health <= 0;
    }

    public int getDamage() {
        return damage;
    }

    public void takeDamage(float amount) {
        health -= amount;
    }

    public void applyKnockback(int dirX) {
        knockbackVX = dirX * 8f;
    }

    public Rectangle getScreenHitbox() {
        return new Rectangle(cameraX, cameraY, hitbox.width, hitbox.height);
    }

    public void reset(int startX, int startY, int startHealth) {
        mapX     = startX;
        mapY     = startY;
        health   = startHealth;
        frame    = 0;
        cameraX  = 0;
        cameraY  = 0;
    }
}

