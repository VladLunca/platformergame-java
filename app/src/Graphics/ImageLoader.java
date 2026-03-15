package Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ImageLoader {
    public static BufferedImage LoadImage(String path)
    {
        try
        {
            return ImageIO.read(Objects.requireNonNull(ImageLoader.class.getResource(path)));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
