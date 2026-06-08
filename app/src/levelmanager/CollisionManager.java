package levelmanager;

import entity.Entity;
import entity.player.Player;

import java.awt.Rectangle;
import java.util.List;

public class CollisionManager {

    public void check(Player player, List<Entity> enemies) {
        Rectangle playerBox = player.getScreenHitbox();
        Rectangle attackBox = player.getAttackHitbox();

        for (Entity e : enemies) {
            if (e.isDead()) continue;
            Rectangle enemyBox = e.getScreenHitbox();

            if (attackBox != null && !player.isAttackHit() && attackBox.intersects(enemyBox)) {
                e.takeDamage(1);
                int knockDir = (e.getMapX() > player.getMapX()) ? 1 : -1;
                e.applyKnockback(knockDir);
                player.setAttackHit(true);
            }

            if (playerBox.intersects(enemyBox)) {
                int knockDir = (player.getMapX() > e.getMapX()) ? 1 : -1;
                player.takeDamage(e.getDamage());
                player.applyKnockback(knockDir);
            }
        }
    }
}
