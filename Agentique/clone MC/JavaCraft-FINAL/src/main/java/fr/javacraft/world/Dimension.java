package fr.javacraft.world;

/**
 * Represents a Minecraft dimension (Overworld, Nether, End).
 * Each dimension has its own world seed and terrain generator.
 * Portals allow traveling between dimensions.
 */
public class Dimension {

    /** Identifiers for each dimension */
    public static final int OVERWORLD = 0;
    public static final int NETHER     = 1;
    public static final int END       = 2;

    private final int id;
    private final long seed;
    private final int seaLevel;
    private final float spawnY;

    public Dimension(int id, long seed) {
        this.id = id;
        this.seed = seed;
        this.seaLevel = (id == NETHER) ? 32 : 62;
        this.spawnY   = (id == NETHER) ? 64.0f : 80.0f;
    }

    public int id()           { return id; }
    public long seed()         { return seed; }
    public int seaLevel()      { return seaLevel; }
    public float spawnY()      { return spawnY; }
    public String name()       {
        return switch(id) {
            case NETHER  -> "Nether";
            case END     -> "End";
            default      -> "Overworld";
        };
    }
}