package entity.enamy;

import entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Snake extends Entity {

    private Map<String, BufferedImage[]> animations;

    public Snake(int mapY, int mapX, int health) {
       super(mapX, mapY, health);
    }
    @Override
    public void update() {

    }

    @Override
    public void onCollision(Entity other) {

    }
    @Override
    public void draw(Graphics g) {

    }
    @Override
    public void drawHitbox(Graphics g) {

    }
}
