package entity.tileeffect;

import entity.player.Player;

public class PlantsEffect implements TileEffect {
    private int bleedTimer  = 0;
    private int lingerTimer = 0;

    @Override
    public int getTileId() { return 3; } // PLANTS_LEVEL1

    @Override
    public void tick(Player player, boolean touching) {
        if (touching) lingerTimer = 120;
        if (lingerTimer > 0) {
            lingerTimer--;
            bleedTimer++;
            if (bleedTimer >= 60) {
                player.directDamage(0.5f);
                bleedTimer = 0;
            }
        } else {
            bleedTimer = 0;
        }
    }

    @Override
    public void reset() {
        bleedTimer  = 0;
        lingerTimer = 0;
    }
}
