package entity.enamy;

import entity.Entity;
import entity.player.Player;
import gamewindow.GameWindow;

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

    public void onCollision(Entity other) {

    }

    @Override
    public void draw(Graphics g, GameWindow wnd, Player p) {

    }

    @Override
    public void draw(Graphics g, GameWindow wnd, map.Map map) {

    }

    @Override
    public void drawHitbox(Graphics g, GameWindow wnd) {

    }

}
