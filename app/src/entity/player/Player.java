package entity.player;

import entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Player extends Entity {
    private static Player instance = null;
    private Map<String, BufferedImage[]> animations;
    private Player(int mapY, int mapX, int health) {
        super(mapX, mapY, health);
        instance = this;
    }
    public static Player createPlayer(int x, int y, int health) {
        if(instance == null) {
            return new Player(x, y, health);
        }
        System.out.println("You can't create 2 players.");
        return instance;
    }
    @Override
    public void update() {}
    @Override
    public void onCollision(Entity other) {}
    @Override
    public void draw(Graphics g) {}
    @Override
    public void drawHitbox(Graphics g) {}
}
