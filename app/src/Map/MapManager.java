package Map;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


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
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
    private void loadMaps() throws IOException {
        String rawJson = readFile();

    }
    private Map getMap(String mapName) {
        return maps.getOrDefault(mapName, null);
    }
    public void drawMap(String mapName, Graphics g) {
    }
}
