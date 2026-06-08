package map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.Entity;
import entity.EntityFactory;
import graphics.tiles.Tile;
import utils.TileID;


import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MapManager {
    private static MapManager instance = null;// avem un singur map manager(Sablon Singletone)
    private static final java.util.Map<String, Map> MAPS = new HashMap<>();
    private final String FILE_NAME;

    public static MapManager createMapManager(String filePath) throws IOException {
        if (instance == null) {
            instance = new
                    MapManager(filePath);
        }
        return instance;
    }

    private MapManager(String path) throws IOException {
        this.FILE_NAME = path;
        instance = this;
        loadMaps();
    }

    private String readFile() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream(this.FILE_NAME))
                )
        )) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    private void loadMaps() throws IOException {
        String rawJson = readFile();
        Gson gson = new Gson();
        JsonObject root = gson.fromJson(rawJson, JsonObject.class);

        for (java.util.Map.Entry<String, JsonElement> entry : root.entrySet()) {
            String mapName = entry.getKey();         // "greenMap", "redMap", etc.
            JsonObject mapObj = entry.getValue().getAsJsonObject();

            int rows = mapObj.get("rows").getAsInt();
            int cols = mapObj.get("cols").getAsInt();

            int[][] grid = new int[rows][cols];
            JsonArray gridArray = mapObj.get("grid").getAsJsonArray();
            for (int i = 0; i < rows; i++) {
                JsonArray row = gridArray.get(i).getAsJsonArray();
                for (int j = 0; j < cols; j++) {
                    grid[i][j] = row.get(j).getAsInt();
                }
            }

            List<Entity> enemies = new ArrayList<>();
            if (mapObj.has("enemies")) {
                JsonArray enemiesArray = mapObj.get("enemies").getAsJsonArray();
                for (JsonElement el : enemiesArray) {
                    JsonObject enemyObj = el.getAsJsonObject();
                    String type = enemyObj.get("type").getAsString();
                    int x = enemyObj.get("x").getAsInt();
                    int y = enemyObj.get("y").getAsInt();
                    int lives = enemyObj.get("lives").getAsInt();
                    int patrol = enemyObj.has("patrol") ? enemyObj.get("patrol").getAsInt() : 2;
                    enemies.add(EntityFactory.create(type, x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT, lives, patrol));
                }
            }
            int gateX = 0, gateY = 0;
            if (mapObj.has("gate")) {
                JsonObject gateObj = mapObj.get("gate").getAsJsonObject();
                gateX = gateObj.get("x").getAsInt() * Tile.TILE_WIDTH;
                gateY = gateObj.get("y").getAsInt() * Tile.TILE_HEIGHT;
            }
            Map m = new Map(mapName, rows, cols, enemies, gateX, gateY);
            m.setGrid(grid);
            MAPS.put(mapName, m);
        }
    }

    public Map getMap(String mapName) {
        return MAPS.getOrDefault(mapName, null);
    }
}
    // Used at debug
 /*   public void drawMap(String mapName, Graphics g) {
        Map map = getMap(mapName);
        if (map != null) {
            int width = map.getWidth();
            int height = map.getHeight();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Tile t = new Tile(Assets.get(TileID.fromId(map.getGrid()[i][j]))[0], map.getGrid()[i][j]);
                    g.drawImage(t.getImage(), j * Tile.TILE_WIDTH, i * Tile.TILE_HEIGHT, Tile.TILE_WIDTH, Tile.TILE_HEIGHT, null);
                }
            }
          //  Entity[] enemies = map.getEnemies().toArray(new Entity[0]);
            for (int i = 0; i < enemies.length; i++) {
           //    enemies[i].draw(g);
            }

        }
    }
}*/
