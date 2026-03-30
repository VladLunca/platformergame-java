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
            enemies = currentMap.getEnemies();
            player= Player.createPlayer(10 * Tile.TILE_WIDTH,15 * Tile.TILE_HEIGHT, 3);
    }
    public void draw(Graphics g, GameWindow wnd) {
        int drawStartX = (player.getMapX() - wnd.GetWndWidth()/ 2) / Tile.TILE_WIDTH;
        int drawStartY = (player.getMapY() - wnd.GetWndHeight() / 2) / Tile.TILE_HEIGHT;
        int drawStopX = (player.getMapX() + wnd.GetWndWidth() / 2) / Tile.TILE_WIDTH;
        int drawStopY =  (player.getMapY()+ wnd.GetWndHeight()/ 2) / Tile.TILE_HEIGHT;
        drawStartX = Math.max(0, drawStartX);
        drawStartY = Math.max(0, drawStartY);
        drawStopX  = Math.min(currentMap.getWidth(),  drawStopX + 1);
        drawStopY  = Math.min(currentMap.getHeight(), drawStopY);
        player.setCameraX(wnd.GetWndWidth()/2) ;
        player.setCameraY(wnd.GetWndHeight()/2) ;

        int camX = player.getMapX() - wnd.GetWndWidth() / 2;
        int camY = player.getMapY() - wnd.GetWndHeight() / 2;
        int offsetX = ((camX % Tile.TILE_WIDTH) + Tile.TILE_WIDTH) % Tile.TILE_WIDTH;
        int offsetY = ((camY % Tile.TILE_HEIGHT) + Tile.TILE_HEIGHT) % Tile.TILE_HEIGHT;
        for (int i = drawStartY; i < drawStopY; i++) {
            for (int j = drawStartX; j < drawStopX; j++) {
                int nr = currentMap.getGrid()[i][j];
                g.drawImage(Assets.get(TileID.fromId(nr))[0],
                        (j - drawStartX) * Tile.TILE_WIDTH - offsetX,
                        (i - drawStartY +1) * Tile.TILE_HEIGHT - offsetY,
                        Tile.TILE_WIDTH, Tile.TILE_HEIGHT, null);
            }
        }
        for(Entity e: enemies){
            e.draw(g,wnd,player);
        }


        player.draw(g,wnd,currentMap);
    }

    public void update() {
        player.update(currentMap);
        for(Entity e: enemies){
            e.update();
        }
    }
}
