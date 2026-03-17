package graphics.assets;

import graphics.utils.ImageLoader;
import graphics.utils.SpriteSheet;

import java.awt.image.BufferedImage;
import java.util.Map;

public class TileAssetsFactory implements AssetsFactory{
    @Override
    public void load(Map<String, BufferedImage[]> images) {
        SpriteSheet level1 = new SpriteSheet(ImageLoader.LoadImage("/textures/maps/level1map.png"));
        images.put("sky",new BufferedImage[] {level1.crop(0, 0)});
        images.put("grassLevel1", new BufferedImage[] { level1.crop(1, 1)});
        images.put("soilLevel1",  new BufferedImage[] { level1.crop(2, 3)});
        images.put("plantsLevel1", new BufferedImage[] {level1.crop(3, 0)});
        images.put("treeLevel1",  new BufferedImage[] { level1.crop(1, 0)});

        SpriteSheet level2 = new SpriteSheet(ImageLoader.LoadImage("/textures/maps/level2map.png"));
        images.put("tortaLevel2", new BufferedImage[] { level2.crop(1, 0)});
        images.put("ladderLevel2",new BufferedImage[] {  level2.crop(2, 0)});
        images.put("soilLevel2", new BufferedImage[] {  level2.crop(2, 3)});
        images.put("grassLevel2", new BufferedImage[] { level2.crop(1, 1)});

        SpriteSheet level3 = new SpriteSheet(ImageLoader.LoadImage("/textures/maps/level3map.png"));
        images.put("darkTreeLevel3",new BufferedImage[] { level3.crop(1, 0)});
        images.put("soilLevel3",    new BufferedImage[] { level3.crop(2, 3)});
        images.put("grassLevel3",  new BufferedImage[] {  level3.crop(1, 1)});
    }
}
