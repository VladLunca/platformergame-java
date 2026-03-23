package utils;

public enum TileID {
    GRASS_LEVEL1   (0,  "grassLevel1",Boolean.TRUE),
    SOIL_LEVEL1   (1,  "soilLevel1",Boolean.TRUE),
    TORTA_LEVEL2    (2,  "tortaLevel2",Boolean.FALSE),
    PLANTS_LEVEL1   (3,  "plantsLevel1",Boolean.FALSE),
    TREE_LEVEL1     (4,  "treeLevel1",Boolean.TRUE),
    SKY  (5,  "sky",Boolean.FALSE),
    LADDER_LEVEL2   (6,  "ladderLevel2",Boolean.TRUE),
    SOIL_LEVEL2     (7,  "soilLevel2",Boolean.TRUE),
    GRASS_LEVEL2    (8,  "grassLevel2",Boolean.TRUE),
    DARK_TREE_LEVEL3(9,  "darkTreeLevel3",Boolean.FALSE),
    SOIL_LEVEL3     (10, "soilLevel3",Boolean.TRUE),
    GRASS_LEVEL3    (11, "grassLevel3",Boolean.TRUE);
    public final int id;
    public final String key;
    private final Boolean isSolid;

    TileID(int id, String key, Boolean isSolid) {
        this.id  = id;
        this.key = key;
        this.isSolid = isSolid;
    }
    public static String fromId(int id) {
        for (TileID t : values())
            if (id==t.id) return t.key;
        throw new IllegalArgumentException("No TileID with id: " + id);
    }
    public static Boolean getSolidFromId(int id) {
        for (TileID t : values())
            if (id==t.id) return t.isSolid;
        throw new IllegalArgumentException("No TileID with id: " + id);
    }
}
