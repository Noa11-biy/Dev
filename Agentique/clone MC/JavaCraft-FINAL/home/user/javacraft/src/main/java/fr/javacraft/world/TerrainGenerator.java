package fr.javacraft.world;

/**
 * Generateur de terrain procedurale.
 * Utilise OpenSimplexNoise multi-octaves pour creer un terrain coherent
 * avec collines, plaines, fonds marins et strates.
 * 
 * Niveau de la mer = 62 (Y). En dessous = eau ou sable.
 * Niveau du bedrock = 0.
 */
public class TerrainGenerator {

    /** Reference a l'instance partagee du bruit */
    private final OpenSimplexNoise noise;

    /** Niveau de la mer (Y) */
    public static final int SEA_LEVEL = 62;

    /** Hauteur minimum du terrain (noise range) */
    private static final double NOISE_MIN = -0.6;
    private static final double NOISE_MAX = 0.6;

    /** Hauteur monde min/max correspondante */
    private static final int TERRAIN_MIN = 3;
    private static final int TERRAIN_MAX = 120;

    /** Graine pour la generation (pour reproductibilite) */
    private final long seed;

    /**
     * Construit le generateur avec une graine donnee.
     * 
     * @param seed  graine entiere
     */
    public TerrainGenerator(long seed) {
        this.seed = seed;
        this.noise = new OpenSimplexNoise(seed);
    }

    /**
     * Remplit le chunk avec le terrain procedural.
     * Applique les strates : bedrock en dessous, pierre, terre, herbe,
     * sable pres de l'eau, eau a partir du niveau de la mer.
     * 
     * @param data  ChunkData a remplir
     */
    public void generate(ChunkData data) {
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                // Coordonnees monde (coin superieur du bloc)
                int worldX = data.cx * 16 + lx;
                int worldZ = data.cz * 16 + lz;

                // Hauteur du terrain avec bruit multi-octaves
                double nx = worldX * 0.01;
                double nz = worldZ * 0.01;
                double n = noise.fbm(nx, nz, 4, 2.0, 0.5);

                // Normalise [0, 1]
                double t = (n - NOISE_MIN) / (NOISE_MAX - NOISE_MIN);
                t = Math.max(0.0, Math.min(1.0, t));

                // Convertit en hauteur monde
                int surfaceHeight = (int)(TERRAIN_MIN + t * (TERRAIN_MAX - TERRAIN_MIN));

                // Remplit la colonne du chunk
                for (int ly = 0; ly < 256; ly++) {
                    BlockType block = getBlockForDepth(ly, surfaceHeight);
                    data.setBlock(lx, ly, lz, block);
                }
            }
        }
    }

    /**
     * Determine le type de bloc en fonction de la profondeur Y
     * et de la hauteur de la surface a cet endroit.
     * 
     * @param ly            coordonnee Y locale [0, 255]
     * @param surfaceHeight  hauteur de la surface a cet endroit
     * @return le BlockType approprie
     */
    private BlockType getBlockForDepth(int ly, int surfaceHeight) {
        // Bedrock (2 dernieres ranges)
        if (ly <= 1) {
            return BlockType.BEDROCK;
        }

        // Au-dessus de la surface
        if (ly > surfaceHeight) {
            if (ly <= SEA_LEVEL) {
                return BlockType.WATER;
            }
            return BlockType.AIR;
        }

        // A la surface
        if (ly == surfaceHeight) {
            if (surfaceHeight <= SEA_LEVEL + 1) {
                return BlockType.SAND;
            }
            return BlockType.GRASS;
        }

        // Sous la surface
        if (ly > surfaceHeight - 4) {
            return BlockType.DIRT;
        }

        // En dessous (roche)
        return BlockType.STONE;
    }

    /**
     * Calcule la hauteur de la surface monde a une position donnee.
     * Utile pour placer le joueur au-dessus du sol.
     * 
     * @param worldX  coordonnee X monde
     * @param worldZ  coordonnee Z monde
     * @return hauteur Y de la surface (bloc sol), ou SEA_LEVEL si sous l'eau
     */
    public int getSurfaceHeight(int worldX, int worldZ) {
        double nx = worldX * 0.01;
        double nz = worldZ * 0.01;
        double n = noise.fbm(nx, nz, 4, 2.0, 0.5);
        double t = (n - NOISE_MIN) / (NOISE_MAX - NOISE_MIN);
        t = Math.max(0.0, Math.min(1.0, t));
        return (int)(TERRAIN_MIN + t * (TERRAIN_MAX - TERRAIN_MIN));
    }
}
