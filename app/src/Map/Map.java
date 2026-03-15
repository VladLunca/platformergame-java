package Map;

import GameWindow.GameWindow;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map {
    private String name;
    private int height;
    private int width;
    private int [][] grid;
    public Map(String name, int height, int width) {
        this.name = name;
        this.height = height;
        this.width = width;
        this.grid = new int [height][width];
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
}

