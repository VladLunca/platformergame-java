package Graphics.Assets;

import Graphics.ImageLoader;
import Graphics.SpriteSheet;

import java.awt.image.BufferedImage;
import java.util.Map;

public class TileFactory implements AssetsFactory{

    @Override
    public void load(Map<String, BufferedImage> images) {
        SpriteSheet level1 = new SpriteSheet(ImageLoader.LoadImage("/textures/maps/level1map.png"));
        images.put("sky",          level1.crop(0, 0));
        images.put("grassLevel1",  level1.crop(1, 1));
        images.put("soilLevel1",   level1.crop(2, 3));
        images.put("plantsLevel1", level1.crop(3, 0));
        images.put("treeLevel1",   level1.crop(1, 0));

        SpriteSheet level2 = new SpriteSheet(ImageLoader.LoadImage("/textures/maps/level2map.png"));
        images.put("tortaLevel2",  level2.crop(1, 0));
        images.put("ladderLevel2",  level2.crop(2, 0));
        images.put("soilLevel2",   level2.crop(2, 3));
        images.put("grassLevel2",  level2.crop(1, 1));

        SpriteSheet level3 = new SpriteSheet(ImageLoader.LoadImage("/textures/maps/level3map.png"));
        images.put("darkTreeLevel3", level3.crop(1, 0));
        images.put("soilLevel3",     level3.crop(2, 3));
        images.put("grassLevel3",    level3.crop(1, 1));
    }
}
