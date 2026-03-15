package graphics.tiles;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Tile {
    public static HashMap<Integer,Tile> usedTiles = new HashMap<Integer,Tile>();
    public static final int TILE_WIDTH  = 32;
    public static final int TILE_HEIGHT = 32;
    private final BufferedImage image;
    private final int id;
    public Tile(BufferedImage image,int id) {
        this.image = image;
        this.id = id;
        if(usedTiles.containsKey(id)) {
            usedTiles.put(id,this);
        }
    }
    public BufferedImage getImage() {
        return image;
    }

}
