package graphics.assets;

import graphics.utils.ImageLoader;
import graphics.utils.SpriteSheet;

import java.awt.image.BufferedImage;
import java.util.Map;

public class PlayerFactory implements AssetsFactory{
    @Override
    public void load(Map<String, BufferedImage[]> images) {
        BufferedImage[] runLeft = new  BufferedImage[8];
        BufferedImage[] runRight = new  BufferedImage[8];;
        BufferedImage[] attackLeft = new  BufferedImage[8]; ;
        BufferedImage[] attackRight = new  BufferedImage[8];;
        for(int i = 0; i < 8;i++){
            attackLeft[i]=ImageLoader.LoadImage("/textures/entities/player/attack/left/2D_KNIGHT__Attack_00"+i+".png");
            attackRight[i]=ImageLoader.LoadImage("/textures/entities/player/attack/right/2D_KNIGHT__Attack_00"+i+".png");
            runLeft[i]=ImageLoader.LoadImage("/textures/entities/player/run/left/2D_KNIGHT__Run_00"+i+".png");
            runRight[i]=ImageLoader.LoadImage("/textures/entities/player/run/right/2D_KNIGHT__Run_00"+i+".png");
        }
        BufferedImage[] jumpLeft = new  BufferedImage[2];
        BufferedImage[] jumpRight = new  BufferedImage[2];
        int i = 0;
        for(String k : new String[]{"Fall_Down", "Jump_Up"}){
            if(i<2){
                jumpLeft[i]=ImageLoader.LoadImage("/textures/entities/player/jump/left/2D_KNIGHT__" + k +"_000.png");
                jumpRight[i]=ImageLoader.LoadImage("/textures/entities/player/jump/right/2D_KNIGHT__" + k +"_000.png");
                i++;
            }

        }
        images.put("playerRunLeft",runLeft);
        images.put("playerRunRight",runRight);
        images.put("playerAttackRight",attackRight);
        images.put("playerAttackLeft",attackLeft);
        images.put("playerJumpLeft",jumpLeft);
        images.put("playerJumpRight",jumpRight);
        images.put("playerIdleRight",new BufferedImage[]{ImageLoader.LoadImage("/textures/entities/player/idle/right.png")});
        images.put("playerIdleLeft",new BufferedImage[]{ImageLoader.LoadImage("/textures/entities/player/idle/left.png")});
        images.put("playerHurtRight",new BufferedImage[]{ImageLoader.LoadImage("/textures/entities/player/hurt/right.png")});
        images.put("playerHurtLeft",new BufferedImage[]{ImageLoader.LoadImage("/textures/entities/player/hurt/left.png")});
    }
}
