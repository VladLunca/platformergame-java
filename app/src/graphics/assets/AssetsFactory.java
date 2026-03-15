package graphics.assets;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface AssetsFactory {
    void load(Map<String, BufferedImage> images);
}
