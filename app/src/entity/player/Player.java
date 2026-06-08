package entity.player;

import entity.Entity;
import entity.utils.PlayerStatus;
import entity.utils.MoveInfo;
import gamewindow.GameWindow;
import graphics.assets.Assets;
import graphics.tiles.Tile;
import handle.KeyHandler;

import entity.tileeffect.TileEffectManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Player extends Entity {
    private PlayerStatus status = PlayerStatus.RIGHT;
    private static volatile Player instance = null;
    private Boolean inAir = false;
    private Boolean attackStarted = false;
    private int slowTimer = 0;
    private final TileEffectManager tileEffectManager = new TileEffectManager();
    private boolean attackHasHit  = false;
    private int hurtTimer         = 0;
    private int invincibilityTimer = 0;
    private static final int INVINCIBILITY_FRAMES = 60;
    private static final int ATTACK_REACH = 55;
    private final Map<String, BufferedImage[]> animations =  new HashMap<String, BufferedImage[]>();
    private float airSpeed=0f;
    private int hitboxOffSet = 40;
    private int playerOffsetX = hitbox.width;
    private int playerOffsetY = 15;
    private Player( int mapX, int mapY, int health) {
        super(mapX, mapY, health);
        this.speed=5;
        hitbox.setSize(20,50);
        animations.put("playerRunLeft", Assets.get("playerRunLeft"));
        animations.put("playerRunRight",Assets.get("playerRunRight"));
        animations.put("playerAttackRight",Assets.get("playerAttackRight"));
        animations.put("playerAttackLeft",Assets.get("playerAttackLeft"));
        animations.put("playerJumpLeft",Assets.get("playerJumpLeft"));
        animations.put("playerJumpRight",Assets.get("playerJumpRight"));
        animations.put("playerIdleRight",Assets.get("playerIdleRight"));
        animations.put("playerIdleLeft",Assets.get("playerIdleLeft"));
        animations.put("playerHurtRight",Assets.get("playerHurtRight"));
        animations.put("playerHurtLeft",Assets.get("playerHurtLeft"));
        instance = this;
    }

    @Override
    public void update(map.Map map) {
        if (invincibilityTimer > 0) invincibilityTimer--;
        if (slowTimer > 0) slowTimer--;
        if (knockbackVX != 0) {
            int newX = mapX + (int) knockbackVX;
            boolean xClear = MoveInfo.moveValid(newX + hitboxOffSet, mapY, map)
                          && MoveInfo.moveValid(newX + hitboxOffSet + hitbox.width, mapY, map)
                          && MoveInfo.moveValid(newX + hitboxOffSet, mapY - hitbox.height, map)
                          && MoveInfo.moveValid(newX + hitboxOffSet + hitbox.width, mapY - hitbox.height, map);
            if (xClear) mapX = newX;
            else knockbackVX = 0;
            knockbackVX *= 0.88f;
            if (Math.abs(knockbackVX) < 0.1f) knockbackVX = 0;
        }
        float gravity=0.02f*Tile.TILE_HEIGHT;
        float fallSpeed=0.05f*Tile.TILE_HEIGHT;
        int speedOnX=0;

        float jumpSpeed=-20;
        int effectiveSpeed = slowTimer > 0 ? speed / 2 : speed;
        if (!attackStarted && hurtTimer <= 0) {
        if (KeyHandler.isMoveLeft())
        {
            status = PlayerStatus.LEFT;
            if(MoveInfo.moveValid(mapX - effectiveSpeed,mapY,map))
                if(MoveInfo.moveValid(mapX - effectiveSpeed, mapY - hitbox.height,map))
                    speedOnX= -effectiveSpeed;
            hitboxOffSet = 0;
            playerOffsetX = -20;
        }
        if(KeyHandler.isMoveRight())
        {
            hitboxOffSet = 40;
            playerOffsetX = hitbox.width;
            status= PlayerStatus.RIGHT;
            if(MoveInfo.moveValid(mapX + hitbox.width + hitboxOffSet + effectiveSpeed,mapY,map))
                if(MoveInfo.moveValid(mapX + hitbox.width + hitboxOffSet + effectiveSpeed, mapY - hitbox.height,map ))
                    speedOnX=effectiveSpeed;
        }
        if(KeyHandler.isJump() && !inAir)
        {
            inAir=true;
            airSpeed=jumpSpeed;
            if(status.compareTo(PlayerStatus.LEFT) == 0 || status.compareTo(PlayerStatus.IDLE_LEFT) == 0 ) {
                status = PlayerStatus.JUMP_LEFT;
            }
            else
            {
                status= PlayerStatus.JUMP_RIGHT;
            }
        }
        }
        mapX=mapX+speedOnX;
        if(MoveInfo.onTheFloor(mapX + hitboxOffSet , mapY, hitbox, map) && inAir && airSpeed > 0)
        {
            inAir = false;
            airSpeed = 0f;
        }
        else {
            inAir = true;
        }
        if (inAir) {
            int checkY = airSpeed < 0
                ? (int)(mapY - hitbox.height + airSpeed)
                : (int)(mapY + airSpeed);
            if (MoveInfo.moveValid(mapX + hitboxOffSet, checkY, map)
                    && MoveInfo.moveValid(mapX + hitboxOffSet + hitbox.width, checkY, map)) {
                mapY += (int) airSpeed;
                airSpeed += gravity;
            } else {
                if (airSpeed < 0) {
                    int ceilRow = checkY / Tile.TILE_HEIGHT;
                    mapY = (ceilRow + 1) * Tile.TILE_HEIGHT + hitbox.height;
                    airSpeed = 0f;
                } else {
                    inAir    = false;
                    airSpeed = 0f;
                }
            }
        }
        else{
            if (status == PlayerStatus.JUMP_LEFT)
            {
                status = PlayerStatus.IDLE_LEFT;
                hitboxOffSet = 0;
                playerOffsetX = - 20;

            }
            if (status == PlayerStatus.JUMP_RIGHT)
            {
                status = PlayerStatus.IDLE_RIGHT;
                hitboxOffSet = 40;
                playerOffsetX = hitbox.width;
            }
        }

        if(KeyHandler.isAttack() && !inAir && !attackStarted && hurtTimer <= 0)
        {
            if(status == PlayerStatus.LEFT || status == PlayerStatus.IDLE_LEFT || status == PlayerStatus.JUMP_LEFT)
            {
                status= PlayerStatus.ATTACK_LEFT;
            }
            else
            {
                status= PlayerStatus.ATTACK_RIGHT;
            }
            attackStarted = true;
            attackHasHit  = false;
            frame = 0;
        }
        frame++;
        if (status == PlayerStatus.ATTACK_RIGHT || status == PlayerStatus.ATTACK_LEFT) {
            BufferedImage[] attackAnim = animations.get(
                status == PlayerStatus.ATTACK_RIGHT ? "playerAttackRight" : "playerAttackLeft"
            );
            if (frame >= attackAnim.length) {
                frame = 0;
                attackStarted = false;
                status = (status == PlayerStatus.ATTACK_RIGHT) ? PlayerStatus.IDLE_RIGHT : PlayerStatus.IDLE_LEFT;
            }
        }
        if (hurtTimer > 0) {
            hurtTimer--;
            if (hurtTimer == 0) {
                status = (status == PlayerStatus.HURT_RIGHT) ? PlayerStatus.IDLE_RIGHT : PlayerStatus.IDLE_LEFT;
            }
        }

        tileEffectManager.update(this, map);
    }


    public static Player getInstance() { return instance; }

    public static Player createPlayer(int mapX, int mapY, int health) {
        if (instance == null) {
            synchronized (Player.class) {
                if (instance == null) {
                    instance = new Player(mapX, mapY, health);
                }
            }
        }
        return instance;
    }

    public void reset(int startX, int startY, int startHealth) {
        mapX                = startX;
        mapY                = startY;
        health              = startHealth;
        status              = PlayerStatus.IDLE_RIGHT;
        inAir               = false;
        attackStarted       = false;
        attackHasHit        = false;
        hurtTimer           = 0;
        airSpeed            = 0f;
        knockbackVX         = 0f;
        invincibilityTimer  = 0;
        slowTimer           = 0;
        tileEffectManager.reset();
        frame               = 0;
        hitboxOffSet        = 40;
        playerOffsetX       = hitbox.width;
    }


    @Override
    public void takeDamage(float amount) {
        if (invincibilityTimer <= 0) {
            health -= amount;
            invincibilityTimer = INVINCIBILITY_FRAMES;
            boolean facingLeft = status == PlayerStatus.LEFT || status == PlayerStatus.IDLE_LEFT
                || status == PlayerStatus.JUMP_LEFT || status == PlayerStatus.ATTACK_LEFT
                || status == PlayerStatus.HURT_LEFT;
            status = facingLeft ? PlayerStatus.HURT_LEFT : PlayerStatus.HURT_RIGHT;
            hurtTimer = 30;
        }
    }

    @Override
    public void applyKnockback(int dirX) {
        knockbackVX = dirX * 10f;
        airSpeed    = -12f;
        inAir       = true;
    }

    @Override
    public Rectangle getScreenHitbox() {
        return new Rectangle(cameraX + hitboxOffSet, cameraY + playerOffsetY, hitbox.width, hitbox.height);
    }

    public Rectangle getAttackHitbox() {
        if (!attackStarted) return null;
        if (status == PlayerStatus.ATTACK_RIGHT)
            return new Rectangle(cameraX + 52, cameraY + 51, 28, 10);
        else
            return new Rectangle(cameraX - 13, cameraY + 51, 28, 10);
    }

    public boolean isAttackHit()           { return attackHasHit; }
    public void    setAttackHit(boolean v) { attackHasHit = v; }

    public void  directDamage(float amount) { health -= amount; }
    public void  applySlow(int frames)      { slowTimer = frames; }
    public int   getHitboxOffset()          { return hitboxOffSet; }

    public void onCollision(Entity other) {
       // health = health - other.damage;
    }

    @Override
    public void draw(Graphics g, GameWindow wnd, Player player, boolean debug) {

        BufferedImage[] image ;
        switch (status) {
            case PlayerStatus.LEFT:
                image=animations.get("playerRunLeft");
                status = PlayerStatus.IDLE_LEFT;
                break;
            case PlayerStatus.RIGHT:
                image=animations.get("playerRunRight");
                status = PlayerStatus.IDLE_RIGHT;
                break;
            case PlayerStatus.IDLE_LEFT:
                image=animations.get("playerIdleLeft");
                status = PlayerStatus.IDLE_LEFT;
                break;
            case PlayerStatus.IDLE_RIGHT:
                image = animations.get("playerIdleRight");
                status = PlayerStatus.IDLE_RIGHT;
                break;
            case PlayerStatus.JUMP_LEFT:
                image = animations.get("playerJumpLeft");
                break;
            case PlayerStatus.JUMP_RIGHT:
                image = animations.get("playerJumpRight");
                break;
            case PlayerStatus.ATTACK_RIGHT:
                image=animations.get("playerAttackRight");
                break;
            case PlayerStatus.ATTACK_LEFT:
                image=animations.get("playerAttackLeft");
                break;
            case PlayerStatus.HURT_RIGHT:
                image=animations.get("playerHurtRight");
                break;
            case PlayerStatus.HURT_LEFT:
                image=animations.get("playerHurtLeft");
                break;
            default:
                image=animations.get("playerIdleLeft");
                break;
        }
        if (debug) drawHitbox(g);
        if(frame >= image.length)
            frame=0;
        g.drawImage(image[frame],cameraX + playerOffsetX, cameraY + playerOffsetY , Tile.TILE_WIDTH,Tile.TILE_HEIGHT,null);
    }

    @Override
    public void draw(Graphics g, GameWindow wnd, map.Map map, boolean debug) {

    }

    @Override
    public void drawHitbox(Graphics g) {
        g.setColor(Color.white);
        g.drawRect(cameraX + hitboxOffSet, cameraY + playerOffsetY, hitbox.width, hitbox.height);

        Rectangle attackBox = getAttackHitbox();
        if (attackBox != null) {
            g.setColor(Color.RED);
            g.drawRect(attackBox.x, attackBox.y, attackBox.width, attackBox.height);
        }
    }
}
