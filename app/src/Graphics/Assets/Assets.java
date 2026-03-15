package Graphics.Assets;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assets {

        private static final Map<String, BufferedImage> images = new HashMap<>();

        public static BufferedImage get(String key) {
            return images.get(key);
        }

        public void Init() {
            List<AssetsFactory> factories = List.of(
                    new TileFactory(),
                    new PropFactory()
            );

            for (AssetsFactory factory : factories) {
                factory.load(images);
            }
        }

}
