package map;

import entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private String name;
    private int height;
    private int width;
    private int [][] grid;
    private List<Entity> enemies = new ArrayList<Entity>();
    private int gateX;
    private int gateY;

    public Map(String name, int height, int width, List<Entity> enemies, int gateX, int gateY) {
        this.name = name;
        this.height = height;
        this.width = width;
        this.grid = new int[height][width];
        this.enemies = enemies;
        this.gateX = gateX;
        this.gateY = gateY;
    }

    public void setGrid(int[][] grid) { this.grid = grid; }
    public int[][] getGrid()          { return grid; }
    public String getName()           { return name; }
    public int getHeight()            { return height; }
    public int getWidth()             { return width; }
    public List<Entity> getEnemies()  { return enemies; }
    public int getGateX()             { return gateX; }
    public int getGateY()             { return gateY; }
}
