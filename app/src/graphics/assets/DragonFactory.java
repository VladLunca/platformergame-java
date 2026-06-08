package graphics.assets;

import graphics.utils.ImageLoader;
import graphics.utils.SpriteSheet;

import java.awt.image.BufferedImage;
import java.util.Map;

public class DragonFactory implements AssetsFactory {

    @Override
    public void load(Map<String, BufferedImage[]> images) {
        loadDragon(images, "dragonGreen",  "/textures/entities/enemies/dragons/green_dragon.png");
        loadDragon(images, "dragonPurple", "/textures/entities/enemies/dragons/purple_dragon.png");
        loadDragon(images, "dragonBlue",   "/textures/entities/enemies/dragons/blue_dragon.png");
        images.put("dragonDead", new BufferedImage[]{ ImageLoader.LoadImage("/textures/entities/enemies/dragons/bones.png") });
    }

    private void loadDragon(Map<String, BufferedImage[]> images, String prefix, String path) {
        SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage(path));
        // row 0: away  row 1: walk right  row 2: toward  row 3: walk left
        images.put(prefix + "Away",      new BufferedImage[]{ sheet.crop(0,0), sheet.crop(1,0), sheet.crop(2,0) });
        images.put(prefix + "WalkRight", new BufferedImage[]{ sheet.crop(0,1), sheet.crop(1,1), sheet.crop(2,1) });
        images.put(prefix + "Toward",    new BufferedImage[]{ sheet.crop(0,2), sheet.crop(1,2), sheet.crop(2,2) });
        images.put(prefix + "WalkLeft",  new BufferedImage[]{ sheet.crop(0,3), sheet.crop(1,3), sheet.crop(2,3) });
    }
}
