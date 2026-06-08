package levelmanager;

import entity.Entity;
import entity.enamy.Dragon;
import entity.utils.DragonTypes;
import entity.player.Player;
import entity.prop.LevelGoal;
import entity.prop.Portal;
import entity.prop.Treasure;
import gamewindow.GameWindow;
import graphics.tiles.Tile;
import map.Map;
import map.MapManager;
import utils.LevelMaps;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LevelManager {
    private static LevelManager instance = null;
    private MapManager mapManager;
    private int level = 1;
    private Player player;
    private List<Entity> enemies = new ArrayList<>();
    private Map currentMap;
    private LevelGoal goal = null;
    private boolean levelWon = false;
    private int currentLives = 3;
    private final HashMap<String, List<int[]>> spawnDataCache = new HashMap<>();

    private final CollisionManager collisionManager = new CollisionManager();
    private final LevelRenderer    renderer         = new LevelRenderer();

    private LevelManager(MapManager mapManager) {
        this.mapManager = mapManager;
        instance = this;
    }

    public static LevelManager createLevelManager(MapManager mapManager) {
        if (instance == null) {
            instance = new LevelManager(mapManager);
        }
        return instance;
    }

    public static LevelManager getInstance() {
        return instance;
    }

    public void setLevel(int level)  { this.level = level; }
    public int  getLevel()           { return level; }
    public void nextLevel()          { if (level < 3) level++; }
    public void resetLives()         { currentLives = 3; }
    public void gainLife()           { if (currentLives < 5) currentLives++; }
    public boolean isLevelWon()      { return levelWon; }
    public boolean isPlayerDead()    { return player != null && player.isDead(); }

    private boolean allDragonsDead() {
        List<Entity> dragons = new ArrayList<>();
        for (Entity e : enemies) {
            if (e instanceof Dragon) dragons.add(e);
        }
        return !dragons.isEmpty() && dragons.stream().allMatch(Entity::isDead);
    }

    public void loadLevel() {
        String mapName = switch (level) {
            case 1  -> LevelMaps.greenMap.name();
            case 2  -> LevelMaps.redMap.name();
            case 3  -> LevelMaps.purpleMap.name();
            default -> throw new IllegalStateException("Unexpected level: " + level);
        };
        currentMap = mapManager.getMap(mapName);
        enemies    = currentMap.getEnemies();

        DragonTypes dragonType = switch (level) {
            case 2  -> DragonTypes.BLUE;
            case 3  -> DragonTypes.PURPLE;
            default -> DragonTypes.GREEN;
        };
        for (Entity e : enemies) {
            if (e instanceof Dragon d) d.setDragonType(dragonType);
        }

        levelWon = false;
        goal = (level == 3)
            ? new Treasure(currentMap.getGateX(), currentMap.getGateY())
            : new Portal(currentMap.getGateX(), currentMap.getGateY());

        if (!spawnDataCache.containsKey(mapName)) {
            List<int[]> data = new ArrayList<>();
            for (Entity e : enemies) {
                data.add(new int[]{e.getMapX(), e.getMapY(), (int) e.getHealth()});
            }
            spawnDataCache.put(mapName, data);
        }

        List<int[]> spawnData = spawnDataCache.get(mapName);
        for (int i = 0; i < enemies.size(); i++) {
            int[] d = spawnData.get(i);
            enemies.get(i).reset(d[0], d[1], d[2]);
        }

        int spawnX = switch (level) {
            case 1  -> 3 * Tile.TILE_WIDTH;
            case 2  -> 5 * Tile.TILE_WIDTH;
            default -> 4 * Tile.TILE_WIDTH;
        };
        int spawnY = switch (level) {
            case 1  -> 10 * Tile.TILE_HEIGHT;
            default -> 13 * Tile.TILE_HEIGHT;
        };

        if (player == null) {
            player = Player.createPlayer(spawnX, spawnY, currentLives);
        } else {
            player.reset(spawnX, spawnY, currentLives);
        }
    }

    public void update() {
        player.update(currentMap);
        for (Entity e : enemies) {
            if (!e.isDead()) e.update(currentMap);
        }
        collisionManager.check(player, enemies);
        if (!levelWon && goal.tryActivate(player, allDragonsDead())) {
            levelWon = true;
        }
    }

    public void draw(Graphics g, GameWindow wnd, boolean debug) {
        renderer.draw(g, wnd, currentMap, player, enemies, goal, allDragonsDead(), debug);
    }
}
