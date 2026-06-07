package graphics.assets;

import graphics.utils.ImageLoader;

import java.awt.image.BufferedImage;
import java.util.Map;

public class UiFactory implements AssetsFactory {
    @Override
    public void load(Map<String, BufferedImage[]> images) {
        images.put("uiMenu",          new BufferedImage[] { ImageLoader.LoadImage("/textures/gamestates/Menu.png") });
        images.put("uiPause",         new BufferedImage[] { ImageLoader.LoadImage("/textures/gamestates/PausedScreen.png") });
        images.put("uiGameOver",      new BufferedImage[] { ImageLoader.LoadImage("/textures/gamestates/GameOverScreen.png") });
        images.put("uiLevelComplete", new BufferedImage[] { ImageLoader.LoadImage("/textures/gamestates/LCompleteScreen.png") });
        images.put("uiGameWon",       new BufferedImage[] { ImageLoader.LoadImage("/textures/gamestates/GameWonScreen.png") });
    }
}
