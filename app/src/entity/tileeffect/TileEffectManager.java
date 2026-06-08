package entity.tileeffect;

import entity.player.Player;
import graphics.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

public class TileEffectManager {
    private final List<TileEffect> effects = new ArrayList<>();

    public TileEffectManager() {
        effects.add(new PlantsEffect());
        effects.add(new TorchEffect());
    }

    public void update(Player player, map.Map map) {
        int topRow   = (player.getMapY() - player.getHitbox().height) / Tile.TILE_HEIGHT;
        int botRow   = player.getMapY() / Tile.TILE_HEIGHT;
        int leftCol  = (player.getMapX() + player.getHitboxOffset()) / Tile.TILE_WIDTH;
        int rightCol = (player.getMapX() + player.getHitboxOffset() + player.getHitbox().width) / Tile.TILE_WIDTH;

        for (TileEffect effect : effects) {
            boolean touching = false;
            outer:
            for (int r = topRow; r <= botRow; r++) {
                if (r < 0 || r >= map.getHeight()) continue;
                for (int c = leftCol; c <= rightCol; c++) {
                    if (c < 0 || c >= map.getWidth()) continue;
                    if (map.getGrid()[r][c] == effect.getTileId()) {
                        touching = true;
                        break outer;
                    }
                }
            }
            effect.tick(player, touching);
        }
    }

    public void reset() {
        for (TileEffect e : effects) e.reset();
    }
}
