package Graphics.Assets;

import Graphics.ImageLoader;
import Graphics.SpriteSheet;

import java.awt.image.BufferedImage;
import java.util.Map;

public class PropFactory implements AssetsFactory{
    @Override
    public void load(Map<String, BufferedImage> images) {
        SpriteSheet gates = new SpriteSheet(ImageLoader.LoadImage("/textures/gates.png"));
        images.put("gateLevel1", gates.crop(0, 0));
        images.put("gateLevel2", gates.crop(1, 0));

        images.put("treasure", ImageLoader.LoadImage("/textures/treasures.png"));
    }
}
