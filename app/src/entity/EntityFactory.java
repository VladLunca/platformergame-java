package entity;

import entity.enamy.Dragon;
import entity.enamy.Snake;
import entity.player.Player;
import utils.EntityTypes;

import java.awt.image.BufferedImage;
import java.util.Map;

public class EntityFactory {
    public static Entity create(EntityTypes type, int x, int y, int health) {
        return switch (type) {
            case EntityTypes.SNAKE  -> new Snake(x, y, health);
            case EntityTypes.DRAGON  -> new Dragon(x, y, health);
            case EntityTypes.PLAYER-> Player.createPlayer(x, y, health);
            default -> throw new IllegalArgumentException("Unknown entity: " + type);
        };
    }
}
