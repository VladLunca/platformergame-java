package graphics.assets;

import graphics.utils.ImageLoader;
import graphics.utils.SpriteSheet;

import java.awt.image.BufferedImage;
import java.util.Map;

public class SnakeFactory implements AssetsFactory{
    @Override
    public void load(Map<String, BufferedImage[]> images) {
        SpriteSheet snakeSpriteSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/entities/enemies/snake/snake.png"));
        BufferedImage[] idle= new  BufferedImage[3];
        BufferedImage[] movingLeft= new  BufferedImage[3];
        BufferedImage[] movingRight= new  BufferedImage[3];
        for(int i=0;i<3;i++){
            idle[i]=snakeSpriteSheet.crop(i,2);
            movingLeft[i]=snakeSpriteSheet.crop(i,3);
            movingRight[i]=snakeSpriteSheet.crop(i,1);
        }
        images.put("snakeIdle",idle);
        images.put("snakeMovingLeft",movingLeft);
        images.put("snakeMovingRight",movingRight);
    }
}
