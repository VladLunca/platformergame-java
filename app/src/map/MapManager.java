package map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import graphics.assets.Assets;
import graphics.tiles.Tile;
import utils.TileID;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Objects;


public class MapManager {
    private static MapManager instance = null;// avem un singur map manager(Sablon Singletone)
    private static final java.util.Map <String,Map> maps =  new HashMap<>();
    private String fileName;
    public static  MapManager createMapManager(String filePath) throws IOException {
        if (instance == null) {
            instance = new MapManager(filePath);
        }
        return instance;
    }
    private MapManager(String path) throws IOException {
        this.fileName = path;
        loadMaps();
    }
    private String readFile() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream(this.fileName))
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
      //System.out.println(rawJson); used at deabug
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
            Map m =new Map(mapName,rows,cols);
            m.setGrid(grid);
            maps.put(mapName, m);
        }

    }
    private Map getMap(String mapName) {
        return maps.getOrDefault(mapName, null);
    }
    public void drawMap(String mapName, Graphics g) {
        Map map = getMap(mapName);
        if (map != null) {
            int width = map.getWidth();
            int height = map.getHeight();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Tile t = new Tile(Assets.get(TileID.fromId(map.getGrid()[i][j])),map.getGrid()[i][j]);
                    g.drawImage(t.getImage(), j * Tile.TILE_WIDTH, i * Tile.TILE_HEIGHT,Tile.TILE_WIDTH,Tile.TILE_HEIGHT,null);
                }
            }
        }
    }
}
