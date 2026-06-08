package entity;

import java.util.HashMap;
import java.util.Map;

public class EntityFactory {

    @FunctionalInterface
    public interface Creator {
        Entity create(int x, int y, int health, int patrolRange);
    }

    private static final Map<String, Creator> registry = new HashMap<>();

    public static void register(String type, Creator creator) {
        registry.put(type.toLowerCase(), creator);
    }

    public static Entity create(String type, int x, int y, int health, int patrolRange) {
        Creator creator = registry.get(type.toLowerCase());
        if (creator == null) throw new IllegalArgumentException("Unknown entity: " + type);
        return creator.create(x, y, health, patrolRange);
    }
}
