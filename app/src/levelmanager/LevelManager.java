package levelmanager;

import entity.Entity;
import entity.player.Player;
import gamewindow.GameWindow;
import graphics.assets.Assets;
import graphics.tiles.Tile;
import map.MapManager;
import utils.LevelMaps;
import map.Map;
import utils.TileID;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class LevelManager {
    private static LevelManager instance = null;
    private MapManager mapManager;
    private int level = 1;
    private Player player;
    private List<Entity> enemies =  new ArrayList<Entity>();
    private Map currentMap;
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
    public void setLevel(int level){
        this.level = level;
    }
    public void loadLevel(){
            currentMap = switch (level){
                case 1 -> mapManager.getMap(LevelMaps.greenMap.name());
                case 2 ->  mapManager.getMap(LevelMaps.redMap.name());
                case 3 ->  mapManager.getMap(LevelMaps.purpleMap.name());
                default -> throw new IllegalStateException("Unexpected value: " + level);
            };
            player = Player.createPlayer(1,1, 3);
    }
    public void draw(Graphics g, GameWindow wnd) {
        player.draw(g,wnd,currentMap);
        for(int i=0; i<currentMap.getHeight();i++) {
            for (int j = 0; j < currentMap.getWidth(); j++) {
                int nr = currentMap.getGrid()[i][j];
                int x = j * Tile.TILE_WIDTH - player.getMapX() + player.getCameraX();
                int y = i * Tile.TILE_HEIGHT - player.getMapY() + player.getCameraY();
                if ((j - 1) * Tile.TILE_WIDTH < player.getMapX() + player.getCameraX() &&
                        (j + 1) * Tile.TILE_WIDTH > player.getMapX() - player.getCameraX() &&
                        (i - 1) * Tile.TILE_HEIGHT < player.getMapY() + player.getCameraY() &&
                        (i + 1) * Tile.TILE_HEIGHT > player.getMapY() - player.getCameraY()
                ) {
                    Tile t = new Tile(Assets.get(TileID.fromId(nr))[0], nr);
                    g.drawImage(t.getImage(), j * Tile.TILE_WIDTH, i * Tile.TILE_HEIGHT, Tile.TILE_WIDTH, Tile.TILE_HEIGHT, null);
                }

            }
        }
        for(Entity e: enemies){
            e.draw(g,wnd,player);
        }
    }

    public void update() {
        player.update();
        for(Entity e: enemies){
            e.update();
        }
    }
}
