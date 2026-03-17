package entity;

import entity.enamy.Dragon;
import entity.enamy.Snake;
import entity.player.Player;

import java.awt.image.BufferedImage;
import java.util.Map;

public class EntityFactory {
    private Map<String,BufferedImage []> animations;

    public EntityFactory(Map<String, BufferedImage[]> animations) {
        this.animations = animations;
    }

    public Entity create(String type, int x, int y, int health) {
        return switch (type) {
            case "snake"  -> new Snake(x, y, health);
            case "dragon"   -> new Dragon(x, y, health);
            case "player" -> Player.createPlayer(x, y, health);
            default       -> throw new IllegalArgumentException("Unknown entity: " + type);
        };
    }
}
