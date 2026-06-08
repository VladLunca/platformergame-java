package entity.tileeffect;

import entity.player.Player;

public interface TileEffect {
    int getTileId();
    void tick(Player player, boolean touching);
    void reset();
}
