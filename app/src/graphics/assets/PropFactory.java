package graphics.assets;

import graphics.utils.ImageLoader;
import graphics.utils.SpriteSheet;

import java.awt.image.BufferedImage;
import java.util.Map;

public class PropFactory implements AssetsFactory{
    @Override
    public void load(Map<String, BufferedImage[]> images) {
        SpriteSheet gates = new SpriteSheet(ImageLoader.LoadImage("/textures/utils/gates.png"));
        images.put("gateLevel1", new BufferedImage[] { gates.crop(0, 0)});
        images.put("gateLevel2", new BufferedImage[] {gates.crop(1, 0)});

        images.put("treasure", new BufferedImage[] {ImageLoader.LoadImage("/textures/utils/treasures.png")});
        images.put("treasureVase", new BufferedImage[] {ImageLoader.LoadImage("/textures/utils/treasures_vase.png")});
        SpriteSheet lives = new SpriteSheet(ImageLoader.LoadImage("/textures/utils/lives.png"));
        images.put("lives", new BufferedImage[] { lives.crop(0, 0),lives.crop(1, 0),lives.crop(2, 0)});
    }
}
