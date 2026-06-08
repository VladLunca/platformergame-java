package entity.tileeffect;

import entity.player.Player;

public class TorchEffect implements TileEffect {

    @Override
    public int getTileId() { return 2; } // TORTA_LEVEL2

    @Override
    public void tick(Player player, boolean touching) {
        if (touching) {
            player.takeDamage(1f);
            player.applySlow(60);
        }
    }

    @Override
    public void reset() {}
}
