package levelmanager;

import entity.Entity;
import entity.player.Player;
import entity.prop.LevelGoal;
import gamewindow.GameWindow;
import graphics.assets.Assets;
import graphics.tiles.Tile;
import map.Map;
import utils.TileID;

import java.awt.Graphics;
import java.util.List;

public class LevelRenderer {

    private final HUD hud = new HUD();

    public void draw(Graphics g, GameWindow wnd, Map map, Player player,
                     List<Entity> enemies, LevelGoal goal,
                     boolean dragonsAllDead, boolean debug) {

        int rawCamX = player.getMapX() - wnd.GetWndWidth()  / 2;
        int rawCamY = player.getMapY() - wnd.GetWndHeight() / 2;

        int maxCamX = map.getWidth()  * Tile.TILE_WIDTH  - wnd.GetWndWidth();
        int maxCamY = map.getHeight() * Tile.TILE_HEIGHT - wnd.GetWndHeight();
        int camX = Math.max(0, Math.min(Math.max(0, maxCamX), rawCamX));
        int camY = Math.max(0, Math.min(Math.max(0, maxCamY), rawCamY));

        player.setCameraX(player.getMapX() - camX);
        player.setCameraY(player.getMapY() - camY);

        int drawStartX = camX / Tile.TILE_WIDTH;
        int drawStartY = camY / Tile.TILE_HEIGHT;
        int drawStopX  = Math.min(map.getWidth(),  (camX + wnd.GetWndWidth())  / Tile.TILE_WIDTH  + 1);
        int drawStopY  = Math.min(map.getHeight(), (camY + wnd.GetWndHeight()) / Tile.TILE_HEIGHT + 1);

        int offsetX = camX % Tile.TILE_WIDTH;
        int offsetY = camY % Tile.TILE_HEIGHT;

        for (int i = drawStartY; i < drawStopY; i++) {
            for (int j = drawStartX; j < drawStopX; j++) {
                int nr = map.getGrid()[i][j];
                g.drawImage(Assets.get(TileID.fromId(nr))[0],
                        (j - drawStartX) * Tile.TILE_WIDTH  - offsetX,
                        (i - drawStartY + 1) * Tile.TILE_HEIGHT - offsetY,
                        Tile.TILE_WIDTH, Tile.TILE_HEIGHT, null);
            }
        }

        goal.draw(g, player, dragonsAllDead);
        for (Entity e : enemies) {
            e.draw(g, wnd, player, debug);
        }
        player.draw(g, wnd, map, debug);
        hud.draw(g, player);
    }
}
