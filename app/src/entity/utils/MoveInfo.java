package entity.utils;

import graphics.tiles.Tile;
import utils.TileID;
import  map.Map;
public class MoveInfo {
    public static boolean moveValid(int x,int y,Map map){
        int px,py;
        px=x/Tile.TILE_WIDTH;
        py=y/Tile.TILE_HEIGHT;
        if(px<0 || py<0) {
            return false;
        }
        else if(px< map.getWidth() && py< map.getHeight())
            return !TileID.getSolidFromId(map.getGrid()[px][py]);
        else
            return false;
    }
    public static int GetUnderOrAbove(int x, int y, float airSpeed)
    {
        int currentTile=(int)((y+1)/ Tile.TILE_HEIGHT);
        if(airSpeed>0)//falling-atinge podeaua
        {
            int tilepPozition=currentTile* Tile.TILE_HEIGHT;
            int offset= 0;//de modificat cand micosram hitboxul a.i. sa cuprinda doar personajul
            return tilepPozition+offset-1;
        }
        else {//jumping
            return currentTile*Tile.TILE_HEIGHT;
        }
    }
    public static boolean OnTheFloor(int x,int y, int playerWidth, Map map){
        int cx1,cx2,cy;
        cx1=(x)/Tile.TILE_WIDTH;
        cx2=(x+playerWidth)/Tile.TILE_WIDTH;
        cy=(y+Tile.TILE_HEIGHT+1)/Tile.TILE_HEIGHT;
        if(cx1>=0 && cy>=0 && cx2< map.getWidth() && cy<map.getHeight() )
        {
            return !(TileID.getSolidFromId(map.getGrid()[cx1][cy]) || TileID.getSolidFromId(map.getGrid()[cx2][cy]));
        }
        return true;

    }
}
