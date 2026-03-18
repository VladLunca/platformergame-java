package map;

import entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private String name;
    private int height;
    private int width;
    private int [][] grid;
    private  List<Entity> enemies =  new ArrayList<Entity>();
    public Map(String name, int height, int width, List<Entity> enemies) {
        this.name = name;
        this.height = height;
        this.width = width;
        this.grid = new int [height][width];
        this.enemies = enemies;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setGrid(int [][] grid) {
        this.grid = grid;
    }
    public int[][] getGrid() {
        return grid;
    }
    public String getName() {
        return name;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }

    public  List<Entity> getEnemies() {
        return enemies;
    }

  // public void addEnamy(){}
}

