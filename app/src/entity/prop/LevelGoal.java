package entity.prop;

import entity.player.Player;

import java.awt.*;

public interface LevelGoal {
    void draw(Graphics g, Player player, boolean dragonsAllDead);
    boolean tryActivate(Player player, boolean dragonsAllDead);
    void reset();
    boolean isActivated();
}
