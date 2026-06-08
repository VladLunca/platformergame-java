package levelmanager;

import entity.Entity;
import entity.enamy.Dragon;
import entity.utils.DragonTypes;
import entity.player.Player;
import entity.prop.LevelGoal;
import entity.prop.Portal;
import entity.prop.Treasure;
import gamewindow.GameWindow;
import graphics.assets.Assets;
import graphics.tiles.Tile;
import map.MapManager;
import utils.LevelMaps;
import map.Map;
import utils.TileID;

import java.awt.*;
import java.awt.image.BufferedImage;
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
    private LevelManager(MapManager mapManager){
        this.mapManager = mapManager;
        instance = this;
    }
    public static LevelManager createLevelManager(MapManager mapManager){
        if(instance==null){
            instance = new LevelManager(mapManager);
        }
        return instance;
    }

    public static LevelManager getInstance() {
        return instance;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void nextLevel() {
        if (level < 3) level++;
    }

    public void resetLives() {
        currentLives = 3;
    }

    public void gainLife() {
        if (currentLives < 5) currentLives++;
    }

    public boolean isLevelWon() {
        return levelWon;
    }

    private boolean allDragonsDead() {
        List<Entity> dragons = new ArrayList<>();
        for (Entity e : enemies) {
            if (e instanceof Dragon) dragons.add(e);
        }
        return !dragons.isEmpty() && dragons.stream().allMatch(Entity::isDead);
    }

    public boolean isPlayerDead() {
        return player != null && player.isDead();
    }

    public void loadLevel() {
        String mapName = switch (level) {
            case 1 -> LevelMaps.greenMap.name();
            case 2 -> LevelMaps.redMap.name();
            case 3 -> LevelMaps.purpleMap.name();
            default -> throw new IllegalStateException("Unexpected value: " + level);
        };
        currentMap = mapManager.getMap(mapName);
        enemies = currentMap.getEnemies();
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
    public void draw(Graphics g, GameWindow wnd, boolean debug) {
        int rawCamX = player.getMapX() - wnd.GetWndWidth() / 2;
        int rawCamY = player.getMapY() - wnd.GetWndHeight() / 2;

        int maxCamX = currentMap.getWidth()  * Tile.TILE_WIDTH  - wnd.GetWndWidth();
        int maxCamY = currentMap.getHeight() * Tile.TILE_HEIGHT - wnd.GetWndHeight();
        int camX = Math.max(0, Math.min(Math.max(0, maxCamX), rawCamX));
        int camY = Math.max(0, Math.min(Math.max(0, maxCamY), rawCamY));

        player.setCameraX(player.getMapX() - camX);
        player.setCameraY(player.getMapY() - camY);

        int drawStartX = camX / Tile.TILE_WIDTH;
        int drawStartY = camY / Tile.TILE_HEIGHT;
        int drawStopX  = Math.min(currentMap.getWidth(),  (camX + wnd.GetWndWidth())  / Tile.TILE_WIDTH  + 1);
        int drawStopY  = Math.min(currentMap.getHeight(), (camY + wnd.GetWndHeight()) / Tile.TILE_HEIGHT + 1);

        int offsetX = camX % Tile.TILE_WIDTH;
        int offsetY = camY % Tile.TILE_HEIGHT;

        for (int i = drawStartY; i < drawStopY; i++) {
            for (int j = drawStartX; j < drawStopX; j++) {
                int nr = currentMap.getGrid()[i][j];
                g.drawImage(Assets.get(TileID.fromId(nr))[0],
                        (j - drawStartX) * Tile.TILE_WIDTH - offsetX,
                        (i - drawStartY + 1) * Tile.TILE_HEIGHT - offsetY,
                        Tile.TILE_WIDTH, Tile.TILE_HEIGHT, null);
            }
        }
        boolean dragonsAllDead = allDragonsDead();
        goal.draw(g, player, dragonsAllDead);
        for (Entity e : enemies) {
            e.draw(g, wnd, player, debug);
        }

        player.draw(g, wnd, currentMap, debug);
        drawHUD(g);
    }

    private void drawHUD(Graphics g) {
        BufferedImage[] hearts   = Assets.get("lives");
        BufferedImage fullHeart  = hearts[0];
        BufferedImage halfHeart  = hearts[1];
        BufferedImage emptyHeart = hearts[2];
        int heartSize = 36;
        int startX    = 12;
        int startY    = 12;
        int spacing   = 36;
        float hp = player.getHealth();
        for (int i = 0; i < 5; i++) {
            BufferedImage img;
            if (i + 1 <= hp)  img = fullHeart;
            else if (i < hp)  img = halfHeart;
            else               img = emptyHeart;
            g.drawImage(img, startX + i * spacing, startY, heartSize, heartSize, null);
        }
    }

    public void update() {
        player.update(currentMap);
        for (Entity e : enemies) {
            if (!e.isDead()) e.update(currentMap);
        }
        checkCollisions();
        if (!levelWon && goal.tryActivate(player, allDragonsDead())) {
            levelWon = true;
        }
    }

    private void checkCollisions() {
        java.awt.Rectangle playerBox  = player.getScreenHitbox();
        java.awt.Rectangle attackBox  = player.getAttackHitbox();

        for (Entity e : enemies) {
            if (e.isDead()) continue;
            java.awt.Rectangle enemyBox = e.getScreenHitbox();

            // Player ataca inamicul
            if (attackBox != null && !player.isAttackHit() && attackBox.intersects(enemyBox)) {
                e.takeDamage(1);
                int knockDir = (e.getMapX() > player.getMapX()) ? 1 : -1;
                e.applyKnockback(knockDir);
                player.setAttackHit(true);
            }

            // Inamicul atinge player-ul
            if (playerBox.intersects(enemyBox)) {
                int knockDir = (player.getMapX() > e.getMapX()) ? 1 : -1;
                player.takeDamage(e.getDamage());
                player.applyKnockback(knockDir);
            }
        }
    }
}
