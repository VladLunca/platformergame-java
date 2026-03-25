package entity.player;

import entity.Entity;
import entity.utils.EntitySatus;
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
    private static Player instance = null;
    private Boolean inAir = false;
    private Boolean attackStarted = false;
    private Map<String, BufferedImage[]> animations =  new HashMap<String, BufferedImage[]>();
    private float airSpeed=0f;
    private Player( int mapX, int mapY, int health) {
        super(mapX, mapY, health);
        this.speed=30;
        hitbox.setSize(40,50);
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

        float jumpSpeed=-19;
        if (KeyHandler.isMoveLeft())
        {
            status = EntitySatus.LEFT;
            if(MoveInfo.moveValid(mapX- Tile.TILE_WIDTH -speed,mapY,map))
                if(MoveInfo.moveValid(mapX- Tile.TILE_WIDTH -speed, mapY - hitbox.height,map))
                    speedOnX= - speed;
        }
        if(KeyHandler.isMoveRight())
        {
            status=EntitySatus.RIGHT;
            if(MoveInfo.moveValid(mapX+hitbox.width+speed,mapY,map))
                if(MoveInfo.moveValid(mapX+hitbox.width+speed, mapY - hitbox.height,map ))
                    speedOnX=speed;
        }
        if(KeyHandler.isJump() && !inAir)
        {
            inAir=true;
            airSpeed=jumpSpeed;
            if(status.compareTo(EntitySatus.LEFT) == 0 || status.compareTo(EntitySatus.IDLE_LEFT) == 0 ) {
                status = EntitySatus.JUMP_LEFT;
            }
            else
            {
                status=EntitySatus.JUMP_RIGHT;
            }
        }
        mapX=mapX+speedOnX;
        if(MoveInfo.onTheFloor(mapX, mapY, hitbox, map) && inAir && airSpeed > 0)
        {
            inAir = false;
            airSpeed = 0f;
        }
        if (inAir) {
            if (MoveInfo.moveValid(mapX, (int)(mapY + airSpeed), map)
                    && MoveInfo.moveValid(mapX + hitbox.width, (int)(mapY + airSpeed), map)) {
                mapY += (int) airSpeed;
                airSpeed += gravity;
            } else {
                inAir    = false;
                airSpeed = 0f;
            }
        }
        else{
            if (status == EntitySatus.JUMP_LEFT)
                status = EntitySatus.IDLE_LEFT;
            if (status == EntitySatus.JUMP_RIGHT)
                status = EntitySatus.IDLE_RIGHT;
        }

        if(KeyHandler.isAttack() && !inAir && !attackStarted)
        {
            if(status == EntitySatus.LEFT)
            {
                status=EntitySatus.ATTACK_LEFT;
            }
            else
            {
                status=EntitySatus.ATTACK_RIGHT;
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
            return new Player(mapX, mapY, health);
        }
        System.out.println("You can't create 2 players.");
        return instance;
    }


    public void onCollision(Entity other) {
       // health = health - other.damage;
    }

    @Override
    public void draw(Graphics g, GameWindow wnd, map.Map map) {
        cameraX= wnd.GetWndWidth()/2;
        cameraY= wnd.GetWndHeight()/2;
        BufferedImage[] image ;
        switch (status) {
            case EntitySatus.LEFT:
                image=animations.get("playerRunLeft");
                status = EntitySatus.IDLE_LEFT;
                break;
            case EntitySatus.RIGHT:
                image=animations.get("playerRunRight");
                status = EntitySatus.IDLE_RIGHT;
                break;
            case EntitySatus.IDLE_LEFT:
                image=animations.get("playerIdleLeft");
                status = EntitySatus.IDLE_LEFT;
                break;
            case EntitySatus.IDLE_RIGHT:
                image = animations.get("playerIdleRight");
                status = EntitySatus.IDLE_RIGHT;
                break;
            case EntitySatus.JUMP_LEFT:
                image = animations.get("playerJumpLeft");
                break;
            case EntitySatus.JUMP_RIGHT:
                image = animations.get("playerJumpRight");
                break;
            case EntitySatus.ATTACK_RIGHT:
                image=animations.get("playerAttackRight");
                status = EntitySatus.IDLE_RIGHT;
                break;
            case EntitySatus.ATTACK_LEFT:
                image=animations.get("playerAttackLeft");
                status = EntitySatus.IDLE_LEFT;
                break;
            default:
                image=animations.get("playerIdleLeft");
                break;
        }
        drawHitbox(g);
        if(frame >= image.length)
            frame=0;
        g.drawImage(image[frame],cameraX, cameraY , Tile.TILE_WIDTH,Tile.TILE_HEIGHT,null);
    }

    private void drawHitbox(Graphics g) {
        g.setColor(Color.white);
        g.drawRect(cameraX, cameraY,hitbox.width,hitbox.height);
    }

    @Override
    public void drawHitbox(Graphics g, GameWindow wnd){}
}
