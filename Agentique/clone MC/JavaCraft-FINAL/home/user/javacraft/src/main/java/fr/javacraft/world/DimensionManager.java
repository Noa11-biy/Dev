package fr.javacraft.world;

/**
 * Gère le changement de dimension via portails.
 * Chaque dimension a son propre ChunkManager (groupe de chunks).
 * Un portail (cadre d'obsidienne 2×3) déclenche le changement.
 */
public class DimensionManager {

    private static DimensionManager INSTANCE;

    private int currentDimension = Dimension.OVERWORLD;
    private final Dimension[] dimensions;

    private DimensionManager() {
        long baseSeed = System.currentTimeMillis();
        this.dimensions = new Dimension[] {
            new Dimension(Dimension.OVERWORLD, baseSeed),
            new Dimension(Dimension.NETHER,    baseSeed + 42),
            new Dimension(Dimension.END,       baseSeed + 84)
        };
    }

    public static DimensionManager getInstance() {
        if (INSTANCE == null) INSTANCE = new DimensionManager();
        return INSTANCE;
    }

    public int currentDimension() { return currentDimension; }

    public void teleportTo(int dimensionId) {
        if (dimensionId < 0 || dimensionId > 2) return;
        if (dimensionId == currentDimension) return;
        this.currentDimension = dimensionId;
        System.out.println("[DimensionManager] Changement de dimension → " + dimensions[dimensionId].name());
        // ChunkManager est recréé/reconfiguré par World
    }

    public Dimension getDimension(int id) { return dimensions[id]; }
    public Dimension getCurrent()         { return dimensions[currentDimension]; }

    /** Tente de créer un portail si les blocs autour correspondent à un cadre obsidienne. */
    public static boolean isPortalFrame(int wx, int wy, int wz, java.util.function.BiFunction<Integer,Integer,Integer> blockAt) {
        // Cadre 2x3 minimum : 4 coins verticaux doivent être de l'obsidienne
        return false; // Simplified: actual portal check in Player interaction
    }
}