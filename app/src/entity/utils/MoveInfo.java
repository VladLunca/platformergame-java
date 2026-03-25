package entity.utils;

import graphics.tiles.Tile;
import utils.TileID;
import  map.Map;

import java.awt.*;

public abstract class MoveInfo {
    public static boolean moveValid(int x,int y,Map map){
        int px,py;
        px=x/Tile.TILE_WIDTH;
        py= y/Tile.TILE_HEIGHT;
        if(px<0 || py<0) {
            return false;
        }
        else if(px< map.getWidth() && py< map.getHeight())
            return !TileID.getSolidFromId(map.getGrid()[py][px]);
        else
            return false;
    }
    public static boolean onTheFloor(int x, int y, Rectangle hitbox, Map map){
        int cx1,cx2,cy;
        cx1=x/Tile.TILE_WIDTH;
        cx2=(x+ (int)hitbox.getWidth())/Tile.TILE_WIDTH;
        cy=(y - hitbox.height)/Tile.TILE_HEIGHT ;
        if(cx1>=0 && cy>=0 && cx2< map.getWidth() && cy<map.getHeight() )
        {
            return TileID.getSolidFromId(map.getGrid()[cy][cx1]) || TileID.getSolidFromId(map.getGrid()[cy][cx2]);
        }
        return true;

    }
}
