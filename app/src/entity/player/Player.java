package entity.player;

import entity.Entity;
import entity.utils.PlayerStatus;
import entity.utils.MoveInfo;
import gamewindow.GameWindow;
import graphics.assets.Assets;
import graphics.tiles.Tile;
import handle.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Player extends Entity {
    private PlayerStatus status = PlayerStatus.RIGHT;
    private static volatile Player instance = null;
    private Boolean inAir = false;
    private Boolean attackStarted = false;
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
    public void update() {

    }

    @Override
    public void update(map.Map map) {
        float gravity=0.02f*Tile.TILE_HEIGHT;
        float fallSpeed=0.05f*Tile.TILE_HEIGHT;
        int speedOnX=0;

        float jumpSpeed=-20;
        if (KeyHandler.isMoveLeft())
        {
            status = PlayerStatus.LEFT;
            if(MoveInfo.moveValid(mapX - speed,mapY,map))
                if(MoveInfo.moveValid(mapX - speed, mapY - hitbox.height,map))
                    speedOnX= -speed;
            hitboxOffSet = 0;
            playerOffsetX = -20;
        }
        if(KeyHandler.isMoveRight())
        {
            hitboxOffSet = 40;
            playerOffsetX = hitbox.width;
            status= PlayerStatus.RIGHT;
            if(MoveInfo.moveValid(mapX + hitbox.width + hitboxOffSet + speed,mapY,map))
                if(MoveInfo.moveValid(mapX + hitbox.width + hitboxOffSet  + speed, mapY - hitbox.height,map ))
                    speedOnX=speed;
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
            if (MoveInfo.moveValid(mapX + hitboxOffSet, (int)(mapY + airSpeed), map)
                    && MoveInfo.moveValid(mapX + hitboxOffSet + hitbox.width, (int)(mapY + airSpeed), map)) {
                mapY += (int) airSpeed;
                airSpeed += gravity;
            } else {
                inAir    = false;
                airSpeed = 0f;
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

        if(KeyHandler.isAttack() && !inAir && !attackStarted)
        {
            if(status == PlayerStatus.LEFT)
            {
                status= PlayerStatus.ATTACK_LEFT;
            }
            else
            {
                status= PlayerStatus.ATTACK_RIGHT;
            }
            attackStarted = true;
            frame=-1;
        }
        frame++;
    }

    @Override
    public void draw(Graphics g, GameWindow wnd, Player p) {

    }

    public static Player createPlayer(int mapX, int mapY, int health) {
        if(instance == null) {
            synchronized (Player.class) {
                if (instance == null) {
                    instance = new Player(mapX, mapY, health);
                }
            }
        }
        System.out.println("You can't create 2 players.");
        return instance;
    }


    public void onCollision(Entity other) {
       // health = health - other.damage;
    }

    @Override
    public void draw(Graphics g, GameWindow wnd, map.Map map) {

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
                status = PlayerStatus.IDLE_RIGHT;
                break;
            case PlayerStatus.ATTACK_LEFT:
                image=animations.get("playerAttackLeft");
                status = PlayerStatus.IDLE_LEFT;
                break;
            default:
                image=animations.get("playerIdleLeft");
                break;
        }
        drawHitbox(g);
        if(frame >= image.length)
            frame=0;
        g.drawImage(image[frame],cameraX + playerOffsetX, cameraY + playerOffsetY , Tile.TILE_WIDTH,Tile.TILE_HEIGHT,null);
    }
    @Override
    public void drawHitbox(Graphics g) {
        g.setColor(Color.white);
        g.drawRect(cameraX + hitboxOffSet, cameraY + playerOffsetY,hitbox.width,hitbox.height);
    }
}
