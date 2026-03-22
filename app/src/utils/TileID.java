package utils;

public enum TileID {
    GRASS_LEVEL1   (0,  "grassLevel1"),
    SOIL_LEVEL1   (1,  "soilLevel1"),
    TORTA_LEVEL2    (2,  "tortaLevel2"),
    PLANTS_LEVEL1   (3,  "plantsLevel1"),
    TREE_LEVEL1     (4,  "treeLevel1"),
    SKY  (5,  "sky"),
    LADDER_LEVEL2   (6,  "ladderLevel2"),
    SOIL_LEVEL2     (7,  "soilLevel2"),
    GRASS_LEVEL2    (8,  "grassLevel2"),
    DARK_TREE_LEVEL3(9,  "darkTreeLevel3"),
    SOIL_LEVEL3     (10, "soilLevel3"),
    GRASS_LEVEL3    (11, "grassLevel3");
    public final int id;
    public final String key;

    TileID(int id, String key) {
        this.id  = id;
        this.key = key;
    }
    public static String fromId(int id) {
        for (TileID t : values())
            if (id==t.id) return t.key;
        throw new IllegalArgumentException("No TileID with id: " + id);
    }

}
