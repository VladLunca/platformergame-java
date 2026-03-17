package entity;

import entity.utils.EntitySatus;

import java.awt.*;

public abstract class Entity {
    protected int mapX;
    protected int mapY;
    protected int cameraX;
    protected int cameraY;
    protected int speed;
    protected int health;
    Rectangle hitbox;
    EntitySatus satus = EntitySatus.RIGHT;

    public Entity(int mapx, int mapy,int health) {
        this.mapX = mapx;
        this.mapY = mapy;
        this.health = health;
    }
    public abstract void update();
    public abstract void onCollision(Entity other);
    public abstract void draw(Graphics g);
    public abstract void drawHitbox(Graphics g);
}

